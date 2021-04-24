package org.jconf.mx.springday.domain.model;

import java.util.Date;

public class ReservationCreated extends AbstractReservation {

  private Date createdAt;

  public ReservationCreated(
      String id, int tableId, String userId, String userName, String tableName, Date createdAt) {
    super(id, tableId, userId, userName, tableName);
    this.createdAt = createdAt;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }
}
