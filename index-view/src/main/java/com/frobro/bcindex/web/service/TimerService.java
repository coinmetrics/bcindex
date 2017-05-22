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
  private DbTickerService dbTickerService;
  private ScheduledExecutorService executor;

  public TimerService(DbTickerService service) {
    this.dbTickerService = service;
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
    run(20);
  }

  public void updateIfInDevMode() {
    if (inDevMode) {
      dbTickerService.updateTickers();
    }
  }

  private void run(long initialDelay) {
    Runnable task = getTask();

    int period = REFRESH_PERIOD_SECONDS;
    executor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.SECONDS);
  }

  private Runnable getTask() {
    return () -> dbTickerService.updateTickers();
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
