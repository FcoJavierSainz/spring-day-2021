package org.jconf.mx.springday.port.persistence;

import org.jconf.mx.springday.configuration.RedisConfiguration;
import org.jconf.mx.springday.domain.model.Profile;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ReactiveRedisProfileRepository extends ReactiveRedisAdapter<Profile> implements ProfileRepository {

  public ReactiveRedisProfileRepository(ReactiveRedisTemplate<String, String> template) {
    super(template.opsForValue(RedisConfiguration.serializer(Profile.class).build()));
  }

  static String profileKey(String userId) {
    return ReactiveRedisAdapter.buildKey("profile", userId);
  }

  @Override
  public Mono<Boolean> saveProfile(String userId, Profile profile) {
    return save(profileKey(userId), profile, getDurationOfHours(10));
  }

  @Override
  public Mono<Profile> getProfile(String userId) {
    return get(profileKey(userId));
  }
}
