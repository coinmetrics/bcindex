package com.frobro.bcindex.web.model.api;

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

  YEARLY {
    @Override
    public int getNumDataPoints() {
      return 524160; // 525600 min/year
    }

    @Override
    public int getTimeStep() {
      return 10080; // num min in a week
    }

    @Override
    public String getTimeStepUnit() {
      return UNIT_WEEK;
    }
  },
  ALL {
    @Override
    public int getNumDataPoints() {
      return 0; // min/day
    }

    @Override
    public int getTimeStep() {
      return 1440;
    }

    public String getTimeStepUnit() {
      throw new UnsupportedOperationException("implement me");
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
}
