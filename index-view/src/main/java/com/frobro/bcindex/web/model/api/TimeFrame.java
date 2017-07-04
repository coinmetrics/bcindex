package com.frobro.bcindex.web.model.api;

import com.frobro.bcindex.core.db.service.BletchDate;
import com.frobro.bcindex.web.service.query.MaxTimeQuery;
import com.frobro.bcindex.web.service.query.TimeSeriesQuery;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Created by rise on 5/12/17.
 */
public enum TimeFrame {
  HOURLY {
    @Override
    public int getNumDataPoints() {
      return (int) BletchDate.MIN_IN_HOUR; // 1 per minute
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
      return (int) BletchDate.MIN_IN_DAY; // 1440 min/day
    }

    @Override
    public int getTimeStep() {
      return (int) BletchDate.MIN_IN_HOUR; // every hour
    }

    @Override
    public String getTimeStepUnit() {
      return UNIT_HOUR;
    }

    @Override
    public long round(long raw) {
      return BletchDate.roundHour(raw);
    }
  },
  WEEKLY {
    @Override
    public int getNumDataPoints() {
      return (int) BletchDate.MIN_IN_WEEK; // 10080 min/week
    }

    @Override
    public int getTimeStep() {
      return (int) BletchDate.MIN_IN_HOUR; // every hour
    }

    @Override
    public String getTimeStepUnit() {
      return UNIT_HOUR;
    }

    @Override
    public long round(long raw) {
      return BletchDate.roundHour(raw);
    }
  },
  MONTHLY {
    @Override
    public int getNumDataPoints() {
      return (int) BletchDate.MIN_IN_MONTH; // 44640 min/month (assuming 31 days)
    }

    @Override
    public int getTimeStep() {
      return (int) BletchDate.MIN_IN_DAY; // num min day
    }

    @Override
    public String getTimeStepUnit() {
      return UNIT_DAY;
    }

    @Override
    public long round(long raw) {
      return BletchDate.roundDay(raw);
    }
  },
  QUARTERLY {
    @Override
    public int getNumDataPoints() {
      return (int) BletchDate.MIN_IN_QUARTER;
    }

    @Override
    public int getTimeStep() {
      return (int) BletchDate.MIN_IN_DAY; // min in day
    }

    public String getTimeStepUnit() {
      return UNIT_DAY;
    }

    @Override
    public long round(long raw) {
      return BletchDate.roundDay(raw);
    }
  },
  YEARLY {
    @Override
    public int getNumDataPoints() {
      return (int) BletchDate.MIN_IN_YEAR;
    }

    @Override
    public int getTimeStep() {
      return (int) BletchDate.MIN_IN_WEEK; // min in week
    }

    public String getTimeStepUnit() {
      return UNIT_WEEK;
    }

    @Override
    public long round(long raw) {
      return BletchDate.roundWeek(raw);
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
      throw new IllegalStateException("Not a valid call for max");
    }

    @Override
    public TimeSeriesQuery getQuery(RequestDto req) {
      return new MaxTimeQuery(req);
    }

    @Override
    public long round(long raw) {
      throw new IllegalStateException("Not a valid call for max");
    }
  };

  protected static final String UNIT_MINUTE = "minute";
  protected static final String UNIT_HOUR = "hour";
  protected static final String UNIT_DAY = "day";
  protected static final String UNIT_WEEK = "week";
  protected static final String UNIT_QUARTER = "quarter";

  abstract public int getTimeStep();
  abstract public int getNumDataPoints();
  abstract public String getTimeStepUnit();
  public long round(long raw) { return raw; }
  public int getModNum() {
    return getNumDataPoints()/getTimeStep();
  }

  public static String getTimeUnitForMax(long size) {
    return null;
  }

  public TimeSeriesQuery getQuery(RequestDto req) {
    return new TimeSeriesQuery(req);
  }
}
