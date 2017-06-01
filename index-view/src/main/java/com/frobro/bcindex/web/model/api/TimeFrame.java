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

    public int getTimeStep() {
      return 1;
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
  };

  abstract public int getTimeStep();
  abstract public int getNumDataPoints();
  public int getModNum() {
    return getNumDataPoints()/getTimeStep();
  }
}
