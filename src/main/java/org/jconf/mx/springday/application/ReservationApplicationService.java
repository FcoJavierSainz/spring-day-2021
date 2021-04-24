package org.jconf.mx.springday.application;

import org.jconf.mx.springday.domain.model.Reservation;
import org.jconf.mx.springday.port.persistence.ReservationRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

@Service
public class ReservationApplicationService implements ReservationApplication {

  private ProfileApplication profileApplication;
  private TableApplication tableApplication;
  private ReservationRepository reservationRepository;

  public ReservationApplicationService(
      ProfileApplication profileApplication,
      TableApplication tableApplication,
      ReservationRepository reservationRepository) {
    this.profileApplication = profileApplication;
    this.tableApplication = tableApplication;
    this.reservationRepository = reservationRepository;
  }

  @Override
  public Mono<Reservation> makeReservation(MakeReservationCommand command) {
    return Mono.zip(
            profileApplication.getProfileById(command.getUserId()),
            tableApplication.getTableById(command.getTableId()))
        .map(
            objects -> {
              command.setUserName(objects.getT1().getName());
              command.setTableName(objects.getT2().getName());
              return Tuples.of(new Reservation(command), objects.getT2());
            })
        .flatMap(
            tuple ->
                reservationRepository
                    .saveReservation(tuple.getT1(), tuple.getT2().getMaxChairs())
                    .map(
                        response -> {
                          if (response.isSuccessful()) {
                            return tuple.getT1();
                          }
                          throw new InvalidTransactionException(
                              "The reservation was not possible, error: " + response.getError());
                        }));
  }

  @Override
  public Mono<Reservation> getReservationByUser(String userId) {
    return reservationRepository
        .getReservationByUserId(userId)
        .switchIfEmpty(
            Mono.error(
                () ->
                    new ResourceNotFoundException("User reservation not found, user: " + userId)));
  }

  @Override
  public Flux<Reservation> getReservationByTable(int tableId) {
    return reservationRepository.getReservationByTable(tableId);
  }

  @Override
  public Mono<Reservation> cancelReservation(String userId) {
    return Mono.zip(
            profileApplication.getProfileById(userId),
            reservationRepository.getReservationByUserId(userId))
        .switchIfEmpty(Mono.error(new ResourceNotFoundException("The reservation was not found")))
        .map(
            objects -> {
              Reservation reservation = objects.getT2();
              reservation.cancel(userId, objects.getT1().getName());
              return reservation;
            })
        .flatMap(
            reservation ->
                reservationRepository
                    .saveCancellation(reservation)
                    .map(
                        response -> {
                          if (response.isSuccessful()) {
                            return reservation;
                          }
                          throw new InvalidTransactionException(
                              "The cancellation was not possible, error: " + response.getError());
                        }));
  }
}
