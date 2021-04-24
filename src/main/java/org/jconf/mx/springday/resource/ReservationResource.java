package org.jconf.mx.springday.resource;

import org.jconf.mx.springday.application.MakeReservationCommand;
import org.jconf.mx.springday.application.ReservationApplication;
import org.jconf.mx.springday.domain.model.Reservation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/reservations")
public class ReservationResource {

  private ReservationApplication service;

  public ReservationResource(ReservationApplication service) {
    this.service = service;
  }

  @PostMapping
  public Mono<Reservation> makeReservation(@RequestBody MakeReservationCommand command) {
    return service.makeReservation(command);
  }

  @GetMapping("/user/{userId}")
  public Mono<Reservation> getReservationByUser(@PathVariable String userId) {
    return service.getReservationByUser(userId);
  }

  @GetMapping("/table/{tableId}")
  public Flux<Reservation> getReservationByTable(@PathVariable int tableId) {
    return service.getReservationByTable(tableId);
  }

  @PostMapping("/cancel/{userId}")
  public Mono<Reservation> cancelReservation(@PathVariable String userId) {
    return service.cancelReservation(userId);
  }
}
