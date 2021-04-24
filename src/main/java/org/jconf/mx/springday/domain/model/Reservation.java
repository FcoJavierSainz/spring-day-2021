package org.jconf.mx.springday.domain.model;

import java.util.Date;
import java.util.UUID;
import org.jconf.mx.springday.application.MakeReservationCommand;

public class Reservation extends AbstractReservation {

  private Date createdAt;
  private Date cancelledAt;
  private boolean active = true;

  public Reservation() {}

  public Reservation(MakeReservationCommand command) {
    super(
        UUID.randomUUID().toString(),
        command.getTableId(),
        command.getUserId(),
        command.getUserName(),
        command.getTableName());
    createdAt = new Date();
  }

  public ReservationCreated creationEvent() {
    return new ReservationCreated(
        getId(), getTableId(), getUserId(), getUserName(), getTableName(), this.createdAt);
  }

  public ReservationCancelled cancellationEvent() {
    return new ReservationCancelled(
        getId(), getTableId(), getUserId(), getUserName(), getTableName(), this.cancelledAt);
  }

  public void cancel(String userId, String userName) {
    this.cancelledAt = new Date();
    setUserId(userId);
    setUserName(userName);
    this.active = false;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getCancelledAt() {
    return cancelledAt;
  }

  public void setCancelledAt(Date cancelledAt) {
    this.cancelledAt = cancelledAt;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
