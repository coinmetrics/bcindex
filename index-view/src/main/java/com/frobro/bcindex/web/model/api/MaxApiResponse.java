package com.frobro.bcindex.web.model.api;

import com.frobro.bcindex.core.db.service.BletchDate;

/**
 * Created by rise on 7/3/17.
 */
public class MaxApiResponse extends ApiResponse {

  private Long totalCount;
  private String queryTimeUnit;
  private TimeFrame queryTimeFrame;

  protected MaxApiResponse() {}

  public ApiResponse setTotalCount(long totalCount) {
    if (this.totalCount == null) {
      this.totalCount = totalCount;
    }
    if (queryTimeFrame == null) {
      queryTimeFrame = setQueryTimeFrame();
      timeFrame = queryTimeFrame;
    }
    if (queryTimeUnit == null) {
      queryTimeUnit = queryTimeFrame.getTimeStepUnit();
      timeUnit = queryTimeUnit;
    }
    return this;
  }

  @Override
  protected long round(long time) {
    return timeFrame.round(time);
  }

  private TimeFrame setQueryTimeFrame() {
    TimeFrame frame;
    if (totalCount <= TimeFrame.HOURLY.getNumDataPoints()) {
      frame = TimeFrame.HOURLY;
    }
    else if (totalCount <= TimeFrame.DAILY.getNumDataPoints()) {
      frame = TimeFrame.DAILY;
    }
    else {
      frame = TimeFrame.MONTHLY;
    }
    return frame;
  }
}
