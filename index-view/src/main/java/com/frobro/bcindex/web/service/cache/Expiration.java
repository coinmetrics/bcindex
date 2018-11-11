package com.frobro.bcindex.web.service.cache;

import com.frobro.bcindex.core.service.BletchDate;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.model.api.IndexType;
import com.frobro.bcindex.web.model.api.TimeFrame;
import com.frobro.bcindex.web.service.query.IndexUpdate;

import java.util.concurrent.TimeUnit;

public class Expiration {
  private static final BcLog LOG = BcLog.getLogger(Expiration.class);
  private final IndexType index;
  private final TimeFrame timeFrame;
  private long lastUpdateTime = 0;

  public Expiration(IndexType idx, TimeFrame tf) {
    this.index = idx;
    this.timeFrame = tf;
  }

  public boolean isExpired(IndexUpdate idxUpdate) {
    long diffMillis = timeSinceLast(idxUpdate.getTimeStamp());

    boolean expired;

    if (isMax(timeFrame)) {
      expired = isMaxExpired(diffMillis, idxUpdate.getMaxBletchId());
    }
    else {
      expired = isExpired(diffMillis);
    }

    if (expired) LOG.debug(index.name() + ", " + timeFrame.name() + " has expired");
    return expired;
  }

  long timeSinceLast(long currentTime) {
    return currentTime - lastUpdateTime;
  }

  private boolean isMax(TimeFrame frame) {
    return TimeFrame.MAX == frame;
  }

  private boolean isExpired(long diffMillis) {
    return timeFrame.timeElapsed(diffMillis);
  }

  private boolean isMaxExpired(long diffMillis, long maxBletchId) {
    return diffMillis > toTimeStep(maxBletchId);
  }

  private long toTimeStep(long maxBletchId) {
    long timeStep;
    if (maxBletchId < TimeFrame.DAILY.getNumDataPoints()) {
      timeStep = TimeFrame.HOURLY.getTimeStep();
    }
    else if (maxBletchId < TimeFrame.WEEKLY.getNumDataPoints()) {
      timeStep = TimeFrame.DAILY.getTimeStep();
    }
    else if (maxBletchId < TimeFrame.MONTHLY.getNumDataPoints()) {
      timeStep = TimeFrame.MONTHLY.getTimeStep();
    }
    else {
      timeStep =  BletchDate.MIN_IN_DAY;
    }
    return TimeUnit.MINUTES.toMillis(timeStep);
  }

  public void updateLastTime(long time) {
    this.lastUpdateTime = time;
  }

  public IndexType getIndex() {
    return index;
  }

  public TimeFrame getTimeFrame() {
    return timeFrame;
  }

  public String getLabel() {
    return index.name() + ":" + timeFrame.name();
  }
  @Override
  public String toString() {
    return "Expiration{" +
        "index=" + index +
        ", timeFrame=" + timeFrame +
        ", lastUpdateTime=" + BletchDate.toDate(lastUpdateTime) +
        '}';
  }
}
