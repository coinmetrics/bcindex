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
  public TimeFrame timeFrame;
  public String timeUnit;

  public double lastPrice = UNINITIALIZED;
  public Double prevClose;
  public Double high = 0.0;
  public Double low = Double.MAX_VALUE;
  public Double change;
  public Double percentChange;

  // graph data
  public final List<Double> data = new LinkedList<>();
  public final List<String> times = new LinkedList<>();

  public static ApiResponse newResponse(RequestDto req) {
    ApiResponse response = new ApiResponse();
    response.currency = req.currency;
    response.index = req.index;
    response.timeFrame = req.timeFrame;
    response.timeUnit = req.timeFrame.getTimeStepUnit();
    return response;
  }

  public ApiResponse addPrice(double px) {
    px = format(px);
    data.add(px);
    checkHigh(px);
    checkLow(px);
    return this;
  }

  private void checkHigh(double px) {
    if (px > high) {
      high = px;
    }
  }

  private void checkLow(double px) {
    if (px < low) {
      low = px;
    }
  }

  public ApiResponse addTime(String time) {
    times.add(time);
    return this;
  }

  public ApiResponse updateLast(double lastPx) {
    this.lastPrice = lastPx;
    return this;
  }

  public void calculateDerivedData() {
    setLastFromList();
    setPrevClose();
    change = lastPrice - prevClose;
    percentChange = (change/prevClose)*100.0;
  }

  private void setLastFromList() {
    if (lastPrice == UNINITIALIZED && data.size() > 0) {
      lastPrice = data.get(data.size()-1);
    }
  }

  private void setPrevClose() {
    if (prevClose == null && data.size() > 0) {
      prevClose = data.get(0);
    }
  }

  public void formatData() {
    lastPrice = format(lastPrice);
    prevClose = format(prevClose);
    high = format(high);
    low = format(low);
    change = format(change);
    percentChange = format(percentChange);
  }

  private double format(double raw) {
    return currency.format(raw);
  }

  public double getLastPrice() {
    return lastPrice;
  }
}
