package org.jconf.mx.springday.resource;

import org.jconf.mx.springday.application.TableApplication;
import org.jconf.mx.springday.domain.model.Table;
import org.jconf.mx.springday.domain.model.TableReservation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/tables")
public class TableResource {

  private TableApplication tableService;

  public TableResource(TableApplication tableService) {
    this.tableService = tableService;
  }

  @GetMapping
  public Flux<Table> getTables() {
    return tableService.getTables();
  }

  @GetMapping("{tableId}/reservations")
  public Mono<TableReservation> getTableReservation(@PathVariable int tableId) {
    return tableService.getTableWithReservations(tableId);
  }
}
