package com.frobro.bcindex.web.service;

import com.frobro.bcindex.core.service.BletchClock;
import com.frobro.bcindex.core.service.BletchDate;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.service.persistence.DailyFireTimes;
import com.frobro.bcindex.web.service.persistence.DailyTimerRepo;

import java.time.Clock;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DailyTimer {
  private static final BcLog LOG = BcLog.getLogger(DailyTimer.class);
  private static DailyTimerRepo repo;
  private Clock clock;
  private final String timerName;
  private long fireCount = 0;
  private long fireTimeMillis;

  public static void setRepo(DailyTimerRepo repoInstance) {
    repo = repoInstance;
  }

  public DailyTimer(String name) {
    this(name, Clock.systemUTC());
  }

  public DailyTimer(String name, Clock clock) {
    this.timerName = name;
    this.clock = clock;
    initialize();
  }

  public boolean shouldFire(long time) {
    return fireTimeMillis - time <= 0;
  }

  public void fired() {
    setPublishTime(clock.millis());
    fireCount++;
  }

  public long getTimesFired() {
    return fireCount;
  }

  private void initialize() {
    if (repo == null) {
      throw new IllegalStateException("trying to initialize " + DailyTimer.class
        + " before the JPA repo is initialized");
    }

    List<DailyFireTimes> data = repo.getLastFireTime(this.timerName);
    // the case data.size == 0 should only happen the first time it
    // runs because there is no data in the db
    fireTimeMillis = BletchDate.getZeroUtcTomorrow(clock);

    if (data.size() == 1){
      long lastScheduledFireTime = data.get(0).getScheduledFireTime();
      // if last fire time is > 24 hours meaning we missed a fire time
      if (fireTimeMillis - lastScheduledFireTime > TimeUnit.DAYS.toMillis(1)) {
        fireTimeMillis = BletchClock.getEpochMillis(); // now
      }
    }
    else if (data.size() > 1){
      throw new IllegalStateException("more than one fire time was return for latest fire time");
    }
  }

  private void setPublishTime(long actualFireTime) {
    long lastFireTime = fireTimeMillis;
    fireTimeMillis = BletchDate.getZeroUtcTomorrow(clock);
    // save fire time
    DailyFireTimes fireTimes = new DailyFireTimes();
    fireTimes.setScheduledFireTime(lastFireTime);
    fireTimes.setTimerName(timerName);
    fireTimes.setActualFireTime(actualFireTime);
    repo.save(fireTimes);
    LOG.info("DailyTimer: " + timerName + " fired event at " + Long.toString(lastFireTime)
      + ". Next fire time is: " + fireTimeMillis);
  }
}
