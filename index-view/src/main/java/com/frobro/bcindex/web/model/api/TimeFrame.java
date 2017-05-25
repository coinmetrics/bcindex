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
    public int getModNum() {
      return 1; // one hour back = 60 / min/per = 60
    }
  },

  DAILY {
    @Override
    public int getNumDataPoints() {
      return 1440; // 1440 min/day / 20 min/point
    }

    @Override
    public int getModNum() {
      return 20; // 1440/60 one day back (assuming minute data)
    }
  },

  WEEKLY {
    @Override
    public int getNumDataPoints() {
      return 10080; // 10080 min/week / 2 hrs/point
    }

    @Override
    public int getModNum() {
      return 120; // one week back
    }
  },

  MONTHLY {
    @Override
    public int getNumDataPoints() {
      return 44640; // 44640 min/month / 6 hrs/point
    }

    @Override
    public int getModNum() {
      return 360; // one week back = num min in 6 hours
    }
  },

  ALL {
    @Override
    public int getNumDataPoints() {
      return 0; // min/day
    }

    @Override
    public int getModNum() {
      return 1440;
    }
  };

  abstract public int getNumDataPoints();
  abstract public int getModNum();
}
