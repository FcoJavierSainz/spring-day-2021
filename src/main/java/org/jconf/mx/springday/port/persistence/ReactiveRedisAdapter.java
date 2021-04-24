package org.jconf.mx.springday.port.persistence;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveSetOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ReactiveRedisAdapter<E> {

  private static final String OBJECT = "object";
  private static final String ID = "id";

  private ReactiveValueOperations<String, E> valueOperations;
  private ReactiveHashOperations<String, String, E> hashOperations;
  private ReactiveSetOperations<String, E> setOperations;

  public ReactiveRedisAdapter(ReactiveHashOperations<String, String, E> hashOperations) {
    this.hashOperations = hashOperations;
  }

  public ReactiveRedisAdapter(ReactiveValueOperations<String, E> valueOperations) {
    this.valueOperations = valueOperations;
  }

  public ReactiveRedisAdapter(ReactiveSetOperations<String, E> setOperations) {
    this.setOperations = setOperations;
  }

  public static String buildKey(String structureType, String key) {
    return buildKey("object:id", structureType, key);
  }

  public static String buildKey(String keyTemplate, String structureType, String key) {
    return keyTemplate.replace(OBJECT, structureType).replace(ID, key);
  }

  protected Mono<Boolean> save(String key, E value) {
    return valueOperations.set(key, value);
  }

  protected Mono<Boolean> saveHash(String key, Map<String, E> value) {
    return hashOperations.putAll(key, value);
  }

  protected Mono<Long> saveSetElements(String key, E[] elements) {
    return setOperations.add(key, elements);
  }

  protected Mono<Boolean> save(String key, E value, Duration expiration) {
    return valueOperations.set(key, value, expiration);
  }

  protected Mono<E> get(String key) {
    return valueOperations.get(key);
  }

  protected Mono<Boolean> isMemberOfSet(String key, E member) {
    return setOperations.isMember(key, member);
  }

  protected Flux<E> getSet(String key) {
    return setOperations.members(key);
  }

  protected Mono<List<E>> multiGet(Collection<String> keys) {
    return valueOperations.multiGet(keys);
  }

  protected <T> Flux<T> getValues(String key, Class<T> clazz) {
    return hashOperations.values(key).cast(clazz);
  }

  protected <T> Mono<T> getHashEntry(String key, String hashKey, Class<T> clazz) {
    return hashOperations.get(key, hashKey).cast(clazz);
  }

  protected Mono<Long> removeSetElements(String key, E[] elements) {
    return setOperations.remove(key, elements);
  }

  protected Duration getDurationOfHours(int hours) {
    return Duration.ofHours(hours);
  }
}
