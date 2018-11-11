package com.frobro.bcindex.web.service.time;

public class TimeService {
  protected static BletchClock clock = new BletchClock();

  public static long currentTimeMillis() {
    return clock.getTimeEpochMillis();
  }

  public static void setClock(BletchClock ck) {
    clock = ck;
  }
}