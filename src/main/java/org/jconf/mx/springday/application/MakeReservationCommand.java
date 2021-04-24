package org.jconf.mx.springday.application;

import org.springframework.validation.annotation.Validated;

@Validated
public class MakeReservationCommand {

  private int tableId;

  private String userId;
  private String userName;
  private String tableName;

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
