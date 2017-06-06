package com.frobro.bcindex.web.model.api;

/**
 * Created by rise on 5/12/17.
 */
public class RequestDto {
  public IndexType index;
  public Currency currency;
  public TimeFrame timeFrame;

  public boolean reqValid() {
    return this.currency != null && this.timeFrame != null
        && this.index != null;
  }

  @Override
  public String toString() {
    return "RequestDto{" +
        "index=" + index +
        ", currency=" + currency +
        ", timeFrame=" + timeFrame +
        '}';
  }
}
