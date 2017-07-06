package com.frobro.bcindex.web.model.api;

import com.frobro.bcindex.core.db.service.BletchDate;
import com.frobro.bcindex.web.bclog.BcLog;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by rise on 5/12/17.
 */
public class ApiResponse {
  private static final BcLog log = BcLog.getLogger(ApiResponse.class);
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
  public List<String> times = new LinkedList<>();

  // data to guarantee last and first elements
  // are in the response
  private String lastTime;
  private String firstTime;
  private Double firstPx;

  protected ApiResponse() {}

  public static ApiResponse newResponse(RequestDto req) {
    ApiResponse response;

    if (req.timeFrame == TimeFrame.MAX) {
      response = new MaxApiResponse();
    }
    else {
      response = new ApiResponse();
    }

    response.currency = req.currency;
    response.index = req.index;
    response.timeFrame = req.timeFrame;
    response.timeUnit = getTimeUnit(req);
    return response;
  }

  private static String getTimeUnit(RequestDto req) {
    String unit = null;
    if (req.timeFrame != TimeFrame.MAX) {
      unit = req.timeFrame.getTimeStepUnit();
    }
    return unit;
  }

  private ApiResponse addPrice(double px) {
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

  private ApiResponse addTime(long time) {
    time = round(time);
    return addTime(BletchDate.toDate(time));
  }

  protected long round(long time) {
    return timeFrame.round(time);
  }

  public ApiResponse addData(double price, long time) {
    addPrice(price);
    addTime(time);
    return this;
  }

  private ApiResponse addTime(String time) {
    times.add(time);
    return this;
  }

  public ApiResponse updateLast(double px, long time) {
    updateLastPx(px);
    updateLastTime(time);
    return this;
  }

  private ApiResponse updateLastPx(double lastPx) {
    this.lastPrice = lastPx;
    return this;
  }

  private ApiResponse updateLastTime(long time) {
    this.lastTime = BletchDate.toDate(round(time));
    return this;
  }

  public ApiResponse updateFirst(double px, long time) {
    updateFirstPx(px);
    updateFirstTime(time);
    return this;
  }

  private ApiResponse updateFirstPx(double px) {
    this.firstPx = px;
    return this;
  }

  private ApiResponse updateFirstTime(long time) {
    this.firstTime = BletchDate.toDate(round(time));
    return this;
  }

  public void calculateDerivedData() {
    if (notNull(firstPx)
        && notNull(firstTime)
        && notNull(lastPrice)
        && notNull(lastTime)) {

      int size = data.size();
      int idx = size-1;
      if (size != times.size()) {
        throw new IllegalStateException("times and data size are different. t=" + times.size() + ", d=" + size);
      }
      data.remove(0);
      times.remove(0);
      data.add(0, firstPx);
      times.add(0, firstTime);

      data.remove(idx);
      times.remove(idx);
      data.add(idx, lastPrice);
      times.add(idx, lastTime);
    }

    setLastFromList();
    setPrevClose();
    checkHighAndLow(lastPrice, prevClose);
    change = lastPrice - prevClose;
    percentChange = (change/prevClose)*100.0;
  }

  private void checkHighAndLow(double lastPrice, double prevClose) {
    checkHigh(lastPrice);
    checkHigh(prevClose);
    checkLow(lastPrice);
    checkLow(prevClose);
  }

  private boolean notNull(Object obj) {
    return obj != null;
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
