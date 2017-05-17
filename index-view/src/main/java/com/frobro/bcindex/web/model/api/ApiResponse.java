package com.frobro.bcindex.web.model.api;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by rise on 5/12/17.
 */
public class ApiResponse {

  public IndexType index;
  public Currency currency;
  public TimeFrame timeUnit;

  public Double lastPrice;
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

  public double getLastPrice() {
    if (lastPrice == null && data.size() > 0) {
      lastPrice = data.get(data.size()-1);
    }
    else {
      throw new IllegalStateException("trying "
          + " to access last price when no prices exist");
    }
    return lastPrice.doubleValue();
  }
}
