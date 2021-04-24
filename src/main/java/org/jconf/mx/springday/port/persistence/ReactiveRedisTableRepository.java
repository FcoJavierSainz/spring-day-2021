package org.jconf.mx.springday.port.persistence;

import java.util.Map;
import org.jconf.mx.springday.configuration.RedisConfiguration;
import org.jconf.mx.springday.domain.model.Table;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ReactiveRedisTableRepository extends ReactiveRedisAdapter<Table> implements TableRepository {

  private static final String TABLES = "tables";

  public ReactiveRedisTableRepository(ReactiveRedisTemplate<String, String> template) {
    super(template.opsForHash(RedisConfiguration.serializer(Table.class).build()));
  }

  @Override
  public Mono<Boolean> saveTables(Map<String, Table> tables) {
    return saveHash(TABLES, tables);
  }

  @Override
  public Flux<Table> getTables() {
    return getValues(TABLES, Table.class);
  }

  @Override
  public Mono<Table> getTableById(int tableId) {
    return getHashEntry(TABLES, String.valueOf(tableId), Table.class);
  }
}
