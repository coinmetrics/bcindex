package com.frobro.bcindex.web.service.publish;

import com.frobro.bcindex.core.service.BletchDate;

import java.time.*;

import static java.util.concurrent.TimeUnit.HOURS;

/**
 * Used for testing to control time
 * Go see Back to the Future if the name makes
 * no sense.
 */
public class DeloreanClock extends Clock {
  private final ZoneId zone;
  private long timeOffset = 0;
  private long initialTime = System.currentTimeMillis() + HOURS.toMillis(1);

  public DeloreanClock() {
    this.zone = ZoneOffset.UTC;
  }

  public DeloreanClock(ZoneId zoneId) {
    this.zone = zoneId;
  }

  public String humanReadableTime() {
    return BletchDate.toDate(millis());
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
    return "DeloreanClock[" + zone + "]";
  }
}
