package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.service.cache.CacheLoader;
import com.frobro.bcindex.web.service.cache.CacheUpdateMgr;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by rise on 5/1/17.
 */
public class TimerService {
  private static final BcLog LOG = BcLog.getLogger(TimerService.class);
  private static final int REFRESH_PERIOD_SECONDS = 10; // 1 minute
  private static final String DEV_MODE = "dev";
  private static final String POSTGRES_DEV_MODE = "pgres";
  private static final BcLog log = BcLog.getLogger(TimerService.class);

  private final boolean inDevMode;
  private final CacheUpdateMgr cache;
  private ScheduledExecutorService executor;

  public TimerService(CacheUpdateMgr cache) {
    this.executor = Executors.newScheduledThreadPool(1);
    inDevMode = decideOnMode();
    this.cache = cache;
  }

  private boolean decideOnMode() {
    boolean state = false;

    String mode = System.getProperty("spring.profiles.active");
    if (mode != null) {
      if (DEV_MODE.equalsIgnoreCase(mode)) {
        state = true;
      }
    }
    return false; //state;
   }

  public void run(CacheLoader cacheLoader) {
    run(cacheLoader, 20);
  }

  public void updateIfInDevMode() {
    if (inDevMode) {
      cache.update();
    }
  }

  private void run(CacheLoader cacheLoader, long initialDelay) {
    // cacheLoader.load needs to be called off the main thread because
    // it takes more than 90 seconds to load. Heroku will
    // raise a
    // Error R10 (Boot timeout) -> Web process failed to bind to $PORT within 90 seconds to launch
    cacheLoader.load();

    if (not(inDevMode)) {
      Runnable task = getTask();
      log.info("starting update thread ----------");

      executor.scheduleAtFixedRate(task, initialDelay, REFRESH_PERIOD_SECONDS, TimeUnit.SECONDS);
    }
  }

  private boolean not(boolean b) {
    return !b;
  }

  private Runnable getTask() {

    return () -> {
      try {
        cache.update();
      } catch (Exception e) {
        LOG.error(e);
      }
    };
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
