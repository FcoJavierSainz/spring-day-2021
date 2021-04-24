package org.jconf.mx.springday.domain.model;

public abstract class AbstractReservation {

  private String id;
  private int tableId;
  private String userId;
  private String userName;
  private String tableName;

  public AbstractReservation() {}

  public AbstractReservation(
      String id, int tableId, String userId, String userName, String tableName) {
    this.id = id;
    this.tableId = tableId;
    this.userId = userId;
    this.userName = userName;
    this.tableName = tableName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public int getTableId() {
    return tableId;
  }

  public void setTableId(int tableId) {
    this.tableId = tableId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
}
