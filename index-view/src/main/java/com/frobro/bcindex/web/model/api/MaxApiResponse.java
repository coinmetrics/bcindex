package com.frobro.bcindex.web.model.api;

/**
 * Created by rise on 7/3/17.
 */
public class MaxApiResponse extends ApiResponse {

  private Long maxBletchId;
  private String queryTimeUnit;
  private TimeFrame queryTimeFrame;

  protected MaxApiResponse() {}

  public long getMaxBletchId() {
    return maxBletchId;
  }

  public ApiResponse setTotalCount(long maxBletchId) {
    if (this.maxBletchId == null) {
      this.maxBletchId = maxBletchId;
    }
    if (queryTimeFrame == null) {
      queryTimeFrame = setQueryTimeFrame(maxBletchId);
      timeFrame = queryTimeFrame;
    }
    if (queryTimeUnit == null) {
      queryTimeUnit = setTimeUnit(queryTimeFrame, maxBletchId);
      timeUnit = queryTimeUnit;
    }
    return this;
  }

  private String setTimeUnit(TimeFrame frame, long maxBletchId) {
    String unit;
    if (maxBletchId <= (TimeFrame.MONTHLY.getNumDataPoints()*2)) {
      unit = frame.getTimeStepUnit();
    }
    else {
      unit = TimeFrame.MAX.getDayTimeUnit();
    }
    return unit;
  }

  private TimeFrame setQueryTimeFrame(long maxBletchId) {
    TimeFrame frame;
    if (maxBletchId <= TimeFrame.DAILY.getNumDataPoints()) {
      frame = TimeFrame.HOURLY;
    }
    else if (maxBletchId <= TimeFrame.WEEKLY.getNumDataPoints()) {
      frame = TimeFrame.DAILY;
    }
    else if (maxBletchId <= (TimeFrame.MONTHLY.getNumDataPoints()*2)) {
      frame = TimeFrame.MONTHLY;
    }
    else {
      frame = TimeFrame.MAX;
    }
    return frame;
  }
}
