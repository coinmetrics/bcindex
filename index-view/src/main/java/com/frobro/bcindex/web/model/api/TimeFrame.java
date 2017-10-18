package com.frobro.bcindex.web.model.api;

import com.frobro.bcindex.core.db.service.BletchDate;
import com.frobro.bcindex.web.service.query.MaxTimeQuery;
import com.frobro.bcindex.web.service.query.TimeSeriesQuery;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

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

    @Override
    public long getTimeSpan() {
      return 3600000;
    }
  },

  DAILY {
    @Override
    public int getNumDataPoints() {
      return (int) BletchDate.MIN_IN_DAY; // 1440 min/day
    }

    @Override
    public int getTimeStep() {
      return 24; // every 24 minutes ~ 60 data points in a day
    }

    @Override
    public String getTimeStepUnit() {
      return UNIT_MINUTE;
    }

    @Override
    public long round(long raw) {
      return BletchDate.roundMinute(raw);
    }

    @Override
    public long getTimeSpan() {
      return 86400000;
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

    @Override
    public long getTimeSpan() {
      return 604800000;
    }
  },
  MONTHLY {
    @Override
    public int getNumDataPoints() {
      return (int) BletchDate.MIN_IN_MONTH; // 44640 min/month (assuming 31 days)
    }

    @Override
    public int getTimeStep() {
      return (int) BletchDate.MIN_IN_12_HOURS;
    }

    @Override
    public String getTimeStepUnit() {
      return UNIT_HOUR;
    }

    @Override
    public long round(long raw) {
      return BletchDate.roundHour(raw);
    }

    @Override
    public long getTimeSpan() {
      return 2678400000L;
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

    @Override
    public long getTimeSpan() {
      return 10713600000L;
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
      return raw;
    }

    @Override
    public long getTimeSpan() {
      throw new IllegalStateException("Not a valid call for max");
    }

    public boolean timeElapsed(long timeDiff, long timeStep) {
      return timeDiff > TimeUnit.MINUTES.toMillis(timeStep);
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
  abstract public long getTimeSpan();
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

  public boolean timeElapsed(long timeDiff) {
    System.out.println("diff=  "+timeDiff
              + "\n" + "thresh " + TimeUnit.MINUTES.toMillis(getTimeStep()));
    System.out.println();
    return timeDiff > TimeUnit.MINUTES.toMillis(getTimeStep());
  }
}
