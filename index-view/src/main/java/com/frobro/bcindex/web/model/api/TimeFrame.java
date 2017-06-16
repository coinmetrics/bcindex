package com.frobro.bcindex.web.model.api;

import com.frobro.bcindex.web.service.query.MaxTimeQuery;
import com.frobro.bcindex.web.service.query.TimeSeriesQuery;

/**
 * Created by rise on 5/12/17.
 */
public enum TimeFrame {
  HOURLY {
    @Override
    public int getNumDataPoints() {
      return 60; // 1 per minute
    }

    @Override
    public int getTimeStep() {
      return 1; // every minute
    }

    @Override
    public String getTimeStepUnit() {
      return UNIT_MINUTE;
    }
  },

  DAILY {
    @Override
    public int getNumDataPoints() {
      return 1440; // 1440 min/day
    }

    @Override
    public int getTimeStep() {
      return 20; // every 20 minutes
    }

    @Override
    public String getTimeStepUnit() {
      return UNIT_MINUTE;
    }
  },

  WEEKLY {
    @Override
    public int getNumDataPoints() {
      return 10080; // 10080 min/week
    }

    @Override
    public int getTimeStep() {
      return 120; // 2 hrs in min
    }

    @Override
    public String getTimeStepUnit() {
      return UNIT_HOUR;
    }
  },

  MONTHLY {
    @Override
    public int getNumDataPoints() {
      return 44640; // 44640 min/month
    }

    @Override
    public int getTimeStep() {
      return 360; // num min in 6 hours
    }

    @Override
    public String getTimeStepUnit() {
      return UNIT_HOUR;
    }
  },
  MAX {
    @Override
    public int getNumDataPoints() {
      throw new IllegalStateException("Not a valid call for max");
    }

    @Override
    public int getTimeStep() {
      throw new IllegalStateException("Not a valid call for max");
    }

    public String getTimeStepUnit() {
      throw new UnsupportedOperationException("implement me");
    }

    @Override
    public TimeSeriesQuery getQuery(RequestDto req) {
      return new MaxTimeQuery(req);
    }
  };

  protected static final String UNIT_MINUTE = "minute";
  protected static final String UNIT_HOUR = "hour";
  protected static final String UNIT_WEEK = "week";

  abstract public int getTimeStep();
  abstract public int getNumDataPoints();
  abstract public String getTimeStepUnit();
  public int getModNum() {
    return getNumDataPoints()/getTimeStep();
  }

  public TimeSeriesQuery getQuery(RequestDto req) {
    return new TimeSeriesQuery(req);
  }
}
