package org.jconf.mx.springday.application;

import org.jconf.mx.springday.domain.model.Reservation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReservationApplication {

  Mono<Reservation> makeReservation(MakeReservationCommand command);

  Mono<Reservation> getReservationByUser(String userId);

  Flux<Reservation> getReservationByTable(int tableId);

  Mono<Reservation> cancelReservation(String userId);
}
