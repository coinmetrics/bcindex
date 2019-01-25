package com.frobro.bcindex.web.service.publish;

import com.frobro.bcindex.core.service.BletchDate;

import java.time.*;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;

/**
 * Used for testing to control the flow of time
 * Go see the movie Back to the Future if the
 * name makes no sense.
 */
public class DeloreanClock extends Clock {
  private final ZoneId zone;
  private long timeOffset = 0;
  private long initialTime = System.currentTimeMillis() + HOURS.toMillis(1);

  public static DeloreanClock getUtcClock() {
    DeloreanClock clock = new DeloreanClock();
    clock.setInitialTime(BletchDate.getZeroUtcToday());
    return clock;
  }

  public DeloreanClock() {
    this.zone = ZoneOffset.UTC;
  }

  public DeloreanClock(ZoneId zoneId) {
    this.zone = zoneId;
  }

  public String humanReadableTime() {
    return BletchDate.toDate(millis());
  }

  public void forwardDays(long days) {
    moveTimeForward(DAYS.toMillis(days));
  }

  public void forwardHours(long hours) {
    moveTimeForward(HOURS.toMillis(hours));
  }

  public void moveTimeForward(long millis) {
    timeOffset += millis;
  }

  public void setInitialTime(long epochMillis) {
    this.initialTime = epochMillis;
  }

  public void reset() {
    timeOffset = 0;
  }

  @Override
  public ZoneId getZone() {
    return zone;
  }

  @Override
  public Clock withZone(ZoneId zone) {
    if (zone.equals(this.zone)) {  // intentional NPE
      return this;
    }
    return new DeloreanClock(zone);
  }

  @Override
  public long millis() {
    return initialTime + timeOffset;
  }

  @Override
  public Instant instant() {
    return Instant.ofEpochMilli(millis());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof DeloreanClock) {
      return zone.equals(((DeloreanClock) obj).zone);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return zone.hashCode() + 1;
  }

  @Override
  public String toString() {
    return "DeloreanClock[" + zone + "] initial: "
        + Instant.ofEpochMilli(initialTime) + ", offset: " + timeOffset
        + " current clock time: " + instant();
  }
}
