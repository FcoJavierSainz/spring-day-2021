package org.jconf.mx.springday.port.persistence;

import org.jconf.mx.springday.domain.model.Reservation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReservationRepository {

  Mono<Response> saveReservation(Reservation reservation, int occupancy);

  Mono<Reservation> getReservationByUserId(String userId);

  Flux<Reservation> getReservationByTable(int tableId);

  Mono<Response> saveCancellation(Reservation reservation);

  class Response {

    private boolean successful;
    private String error;

    public Response(boolean successful, String error) {
      this.successful = successful;
      this.error = error;
    }

    public boolean isSuccessful() {
      return successful;
    }

    public String getError() {
      return error;
    }
  }
}
