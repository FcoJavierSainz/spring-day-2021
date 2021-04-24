package org.jconf.mx.springday.domain.model;

import java.util.List;

public class TableReservation {

  private Table table;
  private List<Reservation> reservations;

  public TableReservation() {}

  public TableReservation(Table table, List<Reservation> reservations) {
    this.table = table;
    this.reservations = reservations;
  }

  public Table getTable() {
    return table;
  }

  public void setTable(Table table) {
    this.table = table;
  }

  public List<Reservation> getReservations() {
    return reservations;
  }

  public void setReservations(List<Reservation> reservations) {
    this.reservations = reservations;
  }
}
