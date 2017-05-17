package com.frobro.bcindex.web.model.api;

/**
 * Created by rise on 5/12/17.
 */
public class RequestDto {
  public IndexType index;
  public Currency currency;
  public TimeFrame timeFrame;

  public boolean isValid() {
    return this.currency != null && this.timeFrame != null
        && this.index != null;
  }
}
