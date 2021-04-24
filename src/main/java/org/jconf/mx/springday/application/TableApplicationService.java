package org.jconf.mx.springday.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jconf.mx.springday.domain.model.Table;
import org.jconf.mx.springday.domain.model.TableReservation;
import org.jconf.mx.springday.port.persistence.ReservationRepository;
import org.jconf.mx.springday.port.persistence.TableOccupancyRepository;
import org.jconf.mx.springday.port.persistence.TableRepository;
import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class TableApplicationService implements TableApplication {

  private static final String TABLE_JSON_PATH = "tables.json";

  private final TableRepository tableRepository;
  private final ReservationRepository reservationRepository;
  private final TableOccupancyRepository occupancyRepository;
  private final ObjectMapper objectMapper;
  private Logger logger;

  public TableApplicationService(
      TableRepository tableRepository,
      ReservationRepository reservationRepository,
      TableOccupancyRepository occupancyRepository,
      ObjectMapper objectMapper,
      Logger logger) {
    this.tableRepository = tableRepository;
    this.reservationRepository = reservationRepository;
    this.occupancyRepository = occupancyRepository;
    this.objectMapper = objectMapper;
    this.logger = logger;
    tableRepository
        .getTables()
        .switchIfEmpty(initTables())
        .subscribeOn(Schedulers.newSingle("Load tables"))
        .subscribe(table -> logger.info("Creating table: {}", table));
  }

  @Override
  public Mono<Table> getTableById(int id) {
    return tableRepository
        .getTableById(id)
        .switchIfEmpty(
            Mono.error(new ResourceNotFoundException("The table does not exist, id: " + id)));
  }

  @Override
  public Flux<Table> getTables() {
    return tableRepository.getTables().collectList().flatMapMany(this::mergeOccupancies);
  }

  private Flux<Table> mergeOccupancies(List<Table> tables) {
    return occupancyRepository
        .getMultipleOccupancies(tables.stream().map(Table::getId).collect(Collectors.toList()))
        .flatMapMany(
            occupancies -> {
              tables.forEach(table -> table.setOccupancy(occupancies.get(table.getId())));
              return Flux.fromIterable(tables);
            });
  }

  private Flux<Table> initTables() {
    return tablesToMap(readFromFile())
        .flatMapMany(
            tableMap ->
                tableRepository
                    .saveTables(tableMap)
                    .flatMapMany(
                        savedTables -> {
                          if (savedTables) {
                            logger.info("Tables loaded in redis: {}", tableMap.size());
                            initOccupancies(tableMap.values());
                            return Flux.fromIterable(tableMap.values());
                          }
                          logger.info("Tables were not loaded in redis");
                          return Flux.empty();
                        }));
  }

  private void initOccupancies(Collection<Table> tables) {
    tables.forEach(table -> occupancyRepository.saveOccupancy(table.getId(), 0).subscribe());
    logger.info("Occupancies were initialized");
  }

  private Mono<Map<String, Table>> tablesToMap(Flux<Table> tables) {
    return tables.collectMap(table -> String.valueOf(table.getId()), Function.identity());
  }

  private Flux<Table> readFromFile() {
    try {
      return Flux.fromIterable(
          objectMapper.readValue(
              new ClassPathResource(TABLE_JSON_PATH).getInputStream(),
              new TypeReference<List<Table>>() {}));
    } catch (IOException io) {
      return Flux.empty();
    }
  }

  @Override
  public Mono<TableReservation> getTableWithReservations(int tableId) {
    return Mono.zip(
            tableRepository.getTableById(tableId),
            occupancyRepository.getTableOccupancy(tableId),
            reservationRepository.getReservationByTable(tableId).collectList())
        .map(
            tuple3 ->
                new TableReservation(tuple3.getT1().addOccupancy(tuple3.getT2()), tuple3.getT3()));
  }
}
