package com.frobro.bcindex.web.model.api;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by rise on 5/12/17.
 */
public class ApiResponse {

  private static final double UNINITIALIZED = -1.0;
  public IndexType index;
  public Currency currency;
  public TimeFrame timeUnit;

  public double lastPrice = UNINITIALIZED;
  public Double prevClose;
  public Double high;
  public Double low;
  public Double change;
  public Double percentChange;

  // graph data
  public final List<Double> data = new LinkedList<>();
  public final List<String> times = new LinkedList<>();

  public ApiResponse addPrice(double px) {
    data.add(px);
    return this;
  }

  public ApiResponse addTime(String time) {
    times.add(time);
    return this;
  }

  public void setLastFromList() {
    if (lastPrice == UNINITIALIZED && data.size() > 0) {
      lastPrice = data.get(data.size()-1);
    }
  }

  public double getLastPrice() {
    return lastPrice;
  }
}
