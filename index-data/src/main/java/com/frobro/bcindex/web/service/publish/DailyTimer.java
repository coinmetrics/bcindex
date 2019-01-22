package com.frobro.bcindex.web.service.publish;

import com.frobro.bcindex.core.service.BletchDate;

import java.time.Clock;

class DailyTimer {
  private Clock clock = Clock.systemUTC();
  private long fireCount = 0;
  private long fireTimeMillis;

  public DailyTimer() {
    resetPublishTime();
  }

  public DailyTimer(Clock clock) {
    this.clock = clock;
    resetPublishTime();
  }

  public boolean shouldFire(long time) {
    return fireTimeMillis - time <= 0;
  }

  public void fire() {
    resetPublishTime();
    fireCount++;
  }

  public long getTimesFired() {
    return fireCount;
  }

  private void resetPublishTime() {
    fireTimeMillis = BletchDate.getZeroUtcTomorrow(clock);
  }

}
