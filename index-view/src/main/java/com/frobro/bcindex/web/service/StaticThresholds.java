package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.model.api.TimeFrame;

public class StaticThresholds {

  public static TimeFrame getTimeFrame(long maxBletchId) {
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
