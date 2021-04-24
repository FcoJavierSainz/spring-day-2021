package org.jconf.mx.springday.domain.model;

import org.springframework.util.ObjectUtils;

public class Table {

  private int id;
  private int x;
  private int y;
  private boolean visible;
  private boolean accessible;
  private String name;
  private int maxChairs;
  private int occupancy;
  private int availability;

  public Table() {}

  public Table(
      int id,
      int x,
      int y,
      boolean visible,
      boolean accessible,
      String name,
      int maxChairs,
      int occupancy,
      int availability) {
    this.id = id;
    this.x = x;
    this.y = y;
    this.visible = visible;
    this.accessible = accessible;
    this.name = name;
    this.maxChairs = maxChairs;
    this.occupancy = occupancy;
    this.availability = availability;
  }

  public String getName() {
    return ObjectUtils.isEmpty(name) ? String.valueOf(id) : name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getAvailability() {
    return maxChairs - occupancy;
  }

  public void setAvailability(int availability) {
    this.availability = availability;
  }

  public Table addOccupancy(int occupancy) {
    setOccupancy(occupancy);
    return this;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public boolean isAccessible() {
    return accessible;
  }

  public void setAccessible(boolean accessible) {
    this.accessible = accessible;
  }

  public int getMaxChairs() {
    return maxChairs;
  }

  public void setMaxChairs(int maxChairs) {
    this.maxChairs = maxChairs;
  }

  public int getOccupancy() {
    return occupancy;
  }

  public void setOccupancy(int occupancy) {
    this.occupancy = occupancy;
  }
}
