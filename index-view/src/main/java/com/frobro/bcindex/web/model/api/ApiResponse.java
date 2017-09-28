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
  public Double high;
  public Double low;
  public Double prevClose;
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
    return newResponse(req.index, req.timeFrame, req.currency);
  }

  public static ApiResponse newResponse(IndexType index,
                                        TimeFrame timeFrame,
                                        Currency currency) {
    ApiResponse response;

    if (timeFrame == TimeFrame.MAX) {
      response = new MaxApiResponse();
    }
    else {
      response = new ApiResponse();
    }

    response.currency = currency;
    response.index = index;
    response.timeFrame = timeFrame;
    response.timeUnit = getTimeUnit(timeFrame);
    return response;
  }

  private static String getTimeUnit(TimeFrame timeFrame) {
    String unit = null;
    if (timeFrame != TimeFrame.MAX) {
      unit = timeFrame.getTimeStepUnit();
    }
    return unit;
  }

  private ApiResponse addPrice(double px) {
    px = format(px);
    data.add(px);
    return this;
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

  public ApiResponse setMaxAndMinPrice(double max, double min) {
    if (high == null) {
      this.high = max;
    }
    if (low == null) {
      this.low = min;
    }
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
    change = lastPrice - prevClose;
    percentChange = (change/prevClose)*100.0;
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
