package com.frobro.bcindex.web.model.api;

import com.frobro.bcindex.core.db.service.BletchDate;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

  public ApiResponse addTime(long time) {
    return addTime(BletchDate.toDate(time));
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
    timeUnit = calculateTimeUnit();
  }

  private String calculateTimeUnit() {
    String firstDate = times.get(0);
    String lastDate = times.get(times.size()-1);
    long fTime = BletchDate.toDate(firstDate).getTime();
    long lTime = BletchDate.toDate(lastDate).getTime();

    long diff = Math.abs(fTime - lTime);

    String unit;
    if (diff <= TimeUnit.HOURS.toMillis(2)) {
      unit = "minute";
    }
    else if (diff <= TimeUnit.DAYS.toMillis(2)) {
      unit = "hour";
    }
    else if (diff <= BletchDate.weeksToMillis(1)) {
      unit = "day";
    }
    else if (diff <= BletchDate.monthsToMillis(1)) {
      unit = "week";
    }
    else if (diff <= BletchDate.monthsToMillis(4)) {
      unit = "month";
    }
    else if (diff <= BletchDate.yearsToMillis(2)) {
      unit = "quarter";
    }
    else {
      unit = "year";
    }
    return unit;
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
