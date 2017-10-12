package com.frobro.bcindex.web.model.api;

import com.frobro.bcindex.core.db.service.BletchDate;
import com.frobro.bcindex.web.bclog.BcLog;

import java.util.LinkedList;
import java.util.List;

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

  private long lastTimeMillis;

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

  public boolean timeElasped(long time) {
    /* implement time frame.timeElasped which
       returns true if this time stamp is past
       the time frame time. for example
       hour --> return TimeUnit.Hour.toMillis <
                (time - getLastTime());
     */
//    return timeFrame.timeElasped(time);
    return false;
  }

  /*
  public double lastPrice = UNINITIALIZED;
  public Double high;
  public Double low;
  public Double prevClose;
  public Double change;
  public Double percentChange;

  private String lastTime;
  private String firstTime;
  private Double firstPx;
   */
  public ApiResponse update(long time, double px) {
//    make sure this is right
    times.remove(0);
    addTime(time);
    lastTime = times.get(times.size()-1);
    firstTime = times.get(0);

    data.remove(0);
    addPrice(px);
    firstPx = data.get(0);
    lastPrice = data.get(data.size()-1);

    if (px > high) {
      high = px;
    }
    else if (px < low) {
      low = px;
    }
    prevClose = firstPx;
    change = lastPrice - prevClose;
    percentChange = (change/prevClose)*100.0;
    return this;
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
    lastTimeMillis = time;
    this.lastTime = BletchDate.toDate(round(lastTimeMillis));
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

  public boolean firstAndLastNotNull() {
    return notNull(firstPx)
        && notNull(firstTime)
        && notNull(lastPrice)
        && notNull(lastTime);
  }

  public void calcAndFormatData() {
    calculateDerivedData();
    formatData();
  }

  public void calculateDerivedData() {
      int size = data.size();
      int idx = size-1;
      if (size != times.size()) {
        throw new IllegalStateException("times and data size are different. t=" + times.size() + ", d=" + size);
      }

      // we don't want to override a
      // valid data point with null
      if (firstAndLastNotNull()) {
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
      change = lastPrice - prevClose.doubleValue();
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

  public long getLatestTime() {
    String timeStr = lastTime != null ? lastTime : times.get(times.size()-1);
    return BletchDate.toEpochMilli(timeStr);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ApiResponse)) return false;

    ApiResponse response = (ApiResponse) o;

    if (Double.compare(response.lastPrice, lastPrice) != 0) return false;
    if (index != response.index) return false;
    if (currency != response.currency) return false;
    if (timeFrame != response.timeFrame) return false;
    if (timeUnit != null ? !timeUnit.equals(response.timeUnit) : response.timeUnit != null) return false;
    if (high != null ? !high.equals(response.high) : response.high != null) return false;
    if (low != null ? !low.equals(response.low) : response.low != null) return false;
    if (prevClose != null ? !prevClose.equals(response.prevClose) : response.prevClose != null) return false;
    if (change != null ? !change.equals(response.change) : response.change != null) return false;
    if (percentChange != null ? !percentChange.equals(response.percentChange) : response.percentChange != null)
      return false;
    if (data != null ? !data.equals(response.data) : response.data != null) return false;
    if (times != null ? !times.equals(response.times) : response.times != null) return false;
    if (lastTime != null ? !lastTime.equals(response.lastTime) : response.lastTime != null) return false;
    if (firstTime != null ? !firstTime.equals(response.firstTime) : response.firstTime != null) return false;
    return !(firstPx != null ? !firstPx.equals(response.firstPx) : response.firstPx != null);
  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = index != null ? index.hashCode() : 0;
    result = 31 * result + (currency != null ? currency.hashCode() : 0);
    result = 31 * result + (timeFrame != null ? timeFrame.hashCode() : 0);
    result = 31 * result + (timeUnit != null ? timeUnit.hashCode() : 0);
    temp = Double.doubleToLongBits(lastPrice);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    result = 31 * result + (high != null ? high.hashCode() : 0);
    result = 31 * result + (low != null ? low.hashCode() : 0);
    result = 31 * result + (prevClose != null ? prevClose.hashCode() : 0);
    result = 31 * result + (change != null ? change.hashCode() : 0);
    result = 31 * result + (percentChange != null ? percentChange.hashCode() : 0);
    result = 31 * result + (data != null ? data.hashCode() : 0);
    result = 31 * result + (times != null ? times.hashCode() : 0);
    result = 31 * result + (lastTime != null ? lastTime.hashCode() : 0);
    result = 31 * result + (firstTime != null ? firstTime.hashCode() : 0);
    result = 31 * result + (firstPx != null ? firstPx.hashCode() : 0);
    return result;
  }
}
