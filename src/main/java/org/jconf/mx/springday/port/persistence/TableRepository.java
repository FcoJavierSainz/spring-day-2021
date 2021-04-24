package org.jconf.mx.springday.port.persistence;

import java.util.Map;
import org.jconf.mx.springday.domain.model.Table;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TableRepository {

  Mono<Boolean> saveTables(Map<String, Table> tables);

  Flux<Table> getTables();

  Mono<Table> getTableById(int tableId);
}
