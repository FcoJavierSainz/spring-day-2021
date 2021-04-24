package org.jconf.mx.springday.domain.model;

public class Profile {
  private String email;
  private String name;
  private Reservation reservation;

  public Profile() {}

  public Profile(String email, String name) {
    this.email = email;
    this.name = name;
  }

  public Profile addReservation(Reservation reservation) {
    setReservation(reservation);
    return this;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Reservation getReservation() {
    return reservation;
  }

  public void setReservation(Reservation reservation) {
    this.reservation = reservation;
  }
}
