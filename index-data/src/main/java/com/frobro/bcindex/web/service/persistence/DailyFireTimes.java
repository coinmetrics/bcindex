package com.frobro.bcindex.web.service.persistence;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "DAILY_FIRE_TIMES")
public class DailyFireTimes {
  @Id
  @GeneratedValue
  private long id;

  private String timerName;
  private long scheduledFireTime;
  private long actualFireTime;

  public long getActualFireTime() {
    return actualFireTime;
  }

  public void setActualFireTime(long actualFireTime) {
    this.actualFireTime = actualFireTime;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTimerName() {
    return timerName;
  }

  public void setTimerName(String timerName) {
    this.timerName = timerName;
  }

  public long getScheduledFireTime() {
    return scheduledFireTime;
  }

  public void setScheduledFireTime(long scheduledFireTime) {
    this.scheduledFireTime = scheduledFireTime;
  }

  @Override
  public String toString() {
    return "DailyFireTimes{" +
        "id=" + id +
        ", timerName='" + timerName + '\'' +
        ", scheduledFireTime=" + scheduledFireTime +
        ", actualFireTime=" + actualFireTime +
        '}';
  }
}
