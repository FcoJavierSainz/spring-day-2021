package org.jconf.mx.springday.domain.model;

import java.util.Date;

public class ReservationCancelled extends AbstractReservation {

  private Date cancelledAt;

  public ReservationCancelled(
      String id, int tableId, String userId, String userName, String tableName, Date cancelledAt) {
    super(id, tableId, userId, userName, tableName);
    this.cancelledAt = cancelledAt;
  }

  public Date getCancelledAt() {
    return cancelledAt;
  }

  public void setCancelledAt(Date cancelledAt) {
    this.cancelledAt = cancelledAt;
  }
}
