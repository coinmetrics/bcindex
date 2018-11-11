package com.frobro.bcindex.web.model.api;

import com.frobro.bcindex.web.service.StaticThresholds;

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
      queryTimeFrame = StaticThresholds.getTimeFrame(maxBletchId);
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
}
