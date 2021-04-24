package org.jconf.mx.springday.application;

import org.jconf.mx.springday.domain.model.Table;
import org.jconf.mx.springday.domain.model.TableReservation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TableApplication {

  Mono<Table> getTableById(int id);

  Flux<Table> getTables();

  Mono<TableReservation> getTableWithReservations(int tableId);
}
