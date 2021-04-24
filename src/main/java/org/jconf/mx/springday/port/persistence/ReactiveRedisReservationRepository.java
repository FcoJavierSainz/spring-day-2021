package org.jconf.mx.springday.port.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.jconf.mx.springday.configuration.RedisConfiguration;
import org.jconf.mx.springday.domain.model.AbstractReservation;
import org.jconf.mx.springday.domain.model.Reservation;
import org.jconf.mx.springday.domain.model.ReservationCancelled;
import org.jconf.mx.springday.domain.model.ReservationCreated;
import org.jconf.mx.springday.domain.model.Table;
import org.jconf.mx.springday.model.TransactionExecutor;
import org.jconf.mx.springday.port.adapter.redis.RedisScriptTransactionExecutor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ReactiveRedisReservationRepository extends ReactiveRedisAdapter<Table> implements ReservationRepository {

  public static final String USER_RESERVATION_KEY = "reservation:users";
  public static final String USER_LOGS_KEY = "logs:users";
  public static final String TABLE_RESERVATION_KEY = "reservation:table";
  public static final String TABLE_LOGS_KEY = "logs:table";
  public static final String QUEUE_LIST_KEY = "queue";
  public static final String RESERVATION_TRANSACTION_NAME = "tableReservation";
  public static final String CANCELLATION_TRANSACTION_NAME = "cancelReservation";
  public static final String OK_RESPONSE = "OK";

  private TransactionExecutor transactionExecutor;
  private ReactiveRedisTemplate<String, String> template;
  private ObjectMapper mapper;

  public ReactiveRedisReservationRepository(
      TransactionExecutor transactionExecutor,
      ReactiveRedisTemplate<String, String> template,
      ObjectMapper mapper) {
    super(template.opsForHash(RedisConfiguration.serializer(Reservation.class).build()));
    this.transactionExecutor = transactionExecutor;
    this.mapper = mapper;
  }

  static String generateTableReservationKey(int tableId) {
    return ReactiveRedisAdapter.buildKey(TABLE_RESERVATION_KEY, String.valueOf(tableId));
  }

  static String generateTableLogsKey(int tableId) {
    return ReactiveRedisAdapter.buildKey(TABLE_LOGS_KEY, String.valueOf(tableId));
  }

  static String generateUserLogsKey(String userId) {
    return ReactiveRedisAdapter.buildKey(USER_LOGS_KEY, userId);
  }

  @Override
  public Mono<Response> saveReservation(Reservation reservation, int occupancy) {
    return transactionExecutor
        .execute(
            RESERVATION_TRANSACTION_NAME,
            getTransactionKeys(reservation),
            getTransactionArgsForReservation(reservation.creationEvent(), occupancy))
        .next()
        .map(this::parseResponse);
  }

  private Response parseResponse(String responseText) {
    if (OK_RESPONSE.equals(responseText)) {
      return new Response(true, null);
    }
    return new Response(false, responseText);
  }

  private List<Object> getTransactionArgsForReservation(
      ReservationCreated reservation, Integer occupancy) {
    ArrayList<Object> list = new ArrayList<>(3);
    list.add(String.valueOf(occupancy));
    list.add(reservation.getUserId());
    list.add(formatAsJson(reservation));
    return list;
  }

  private String formatAsJson(AbstractReservation reservation) {
    try {
      return mapper.writeValueAsString(reservation);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Invalid json", e);
    }
  }

  private List<String> getTransactionKeys(AbstractReservation reservation) {
    ArrayList<String> list = new ArrayList<>(7);
    list.add(ReactiveRedisProfileRepository.profileKey(reservation.getUserId()));
    list.add(ReactiveRedisTableOccupancyRepository.tableOccupancyId(reservation.getTableId()));
    list.add(USER_RESERVATION_KEY);
    list.add(generateTableReservationKey(reservation.getTableId()));
    list.add(generateTableLogsKey(reservation.getTableId()));
    list.add(generateUserLogsKey(reservation.getUserId()));
    list.add(QUEUE_LIST_KEY);
    return list;
  }

  @Override
  public Mono<Reservation> getReservationByUserId(String userId) {
    return getHashEntry(USER_RESERVATION_KEY, String.valueOf(userId), Reservation.class);
  }

  @Override
  public Flux<Reservation> getReservationByTable(int tableId) {
    return getValues(buildKey(TABLE_RESERVATION_KEY, String.valueOf(tableId)), Reservation.class);
  }

  @Override
  public Mono<Response> saveCancellation(Reservation reservation) {
    return transactionExecutor
        .execute(
            CANCELLATION_TRANSACTION_NAME,
            getTransactionKeys(reservation),
            getTransactionArgsCancellation(reservation.cancellationEvent()))
        .next()
        .map(this::parseResponse);
  }

  private List<Object> getTransactionArgsCancellation(ReservationCancelled reservation) {
    ArrayList<Object> list = new ArrayList<>(2);
    list.add(reservation.getUserId());
    list.add(formatAsJson(reservation));
    return list;
  }

}
