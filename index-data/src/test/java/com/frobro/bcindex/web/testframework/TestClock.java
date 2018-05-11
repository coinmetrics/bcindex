package com.frobro.bcindex.web.testframework;

import static java.util.concurrent.TimeUnit.*;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static java.util.concurrent.TimeUnit.HOURS;

public class TestClock extends Clock {
  private final ZoneId zone;
  private long timeOffset = 0;
  private long initialTime = System.currentTimeMillis() + HOURS.toMillis(1);

  public TestClock(ZoneId zone) {
    this.zone = zone;
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
    return new TestClock(zone);
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
    if (obj instanceof TestClock) {
      return zone.equals(((TestClock) obj).zone);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return zone.hashCode() + 1;
  }

  @Override
  public String toString() {
    return "TestClock[" + zone + "]";
  }
}
