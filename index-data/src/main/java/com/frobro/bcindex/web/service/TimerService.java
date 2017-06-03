package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by rise on 5/1/17.
 */
public class TimerService {
  private static final int REFRESH_PERIOD_SECONDS = 60; // 1 minute
  private static final String DEV_MODE = "dev";
  private static final String POSTGRES_DEV_MODE = "pgres";
  private static final BcLog log = BcLog.getLogger(TimerService.class);

  private final boolean inDevMode;
  private TickerService tickerService;
  private ScheduledExecutorService executor;

  public TimerService(TickerService service) {
    this.tickerService = service;
    this.executor = Executors.newScheduledThreadPool(1);
    inDevMode = decideOnMode();
  }

  private boolean decideOnMode() {
    boolean state = false;

    String mode = System.getProperty("spring.profiles.active");
    if (mode != null) {
      if (DEV_MODE.equalsIgnoreCase(mode) || POSTGRES_DEV_MODE.equalsIgnoreCase(mode)) {
        state = true;
      }
    }
    return state;
   }

  public void run() {
    if (not(inDevMode)) {
      startScheduleThread();
    }
  }

  private boolean not(boolean b) {
    return !b;
  }

  private void startScheduleThread() {
    Runnable task = getTask();

    long initialDelay = 0;
    int period = REFRESH_PERIOD_SECONDS;

    log.info("starting scheduler to make rest calls every "
        + period + " seconds");

    executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
  }

  private Runnable getTask() {
    return () -> tickerService.updateTickers();
  }

  public void shutdown() {
    log.info("shutting down executor");
    try {
      executor.shutdown();
      executor.awaitTermination(5, TimeUnit.SECONDS);
    } catch (InterruptedException ie) {
      log.error(ie);
    }
    finally {
      executor.shutdownNow();
      log.info("shutown complete");
    }
  }
}
