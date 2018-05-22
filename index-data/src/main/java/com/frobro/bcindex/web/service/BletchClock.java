package com.frobro.bcindex.web.service;

import java.time.Clock;

public class BletchClock {
  private static Clock clock = Clock.systemDefaultZone();

  public static void setClock(Clock c) {
    clock = c;
  }

  public static long getTimeEpochMillis() {
    return clock.millis();
  }
}
