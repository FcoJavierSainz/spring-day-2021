package org.jconf.mx.springday.port.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.jconf.mx.springday.configuration.RedisConfiguration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ReactiveRedisTableOccupancyRepository extends ReactiveRedisAdapter<Integer> implements
    TableOccupancyRepository {

  private static final String TABLE_OCCUPANCY = "table:occupancy";
  private final ReactiveRedisTemplate<String, String> template;

  public ReactiveRedisTableOccupancyRepository(ReactiveRedisTemplate<String, String> template) {
    super(template.opsForValue(RedisConfiguration.serializer(Integer.class).build()));
    this.template = template;
  }

  static String tableOccupancyId(Integer tableId) {
    return buildKey(TABLE_OCCUPANCY, String.valueOf(tableId));
  }

  @Override
  public Mono<Boolean> saveOccupancy(Integer tableId, Integer occupancy) {
    return save(tableOccupancyId(tableId), occupancy);
  }

  @Override
  public Mono<Map<Integer, Integer>> getMultipleOccupancies(List<Integer> keys) {
    return multiGet(
            keys.stream()
                .map(ReactiveRedisTableOccupancyRepository::tableOccupancyId)
                .collect(Collectors.toList()))
        .map(
            values -> {
              Map<Integer, Integer> map = new HashMap<>();
              for (int i = 0; i < keys.size(); i++) {
                map.put(keys.get(i), values.get(i));
              }
              return map;
            });
  }

  @Override
  public Mono<Integer> getTableOccupancy(Integer tableId) {
    return get(tableOccupancyId(tableId));
  }

  private Integer getKey(String key) {
    return Integer.parseInt(key.replace(TABLE_OCCUPANCY, ""));
  }
}
