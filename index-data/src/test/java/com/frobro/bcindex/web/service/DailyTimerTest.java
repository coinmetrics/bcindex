package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.service.persistence.DailyTimerRepo;
import com.frobro.bcindex.web.service.publish.DeloreanClock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DailyTimerTest {

  private DailyTimerRepo repo;

  @Autowired
  public void setup(DailyTimerRepo repo) {
    this.repo = repo;
    DailyTimer.setRepo(this.repo);
  }

  @After
  public void tearDown() {
    repo.deleteAll();
  }

  @Test
  public void testDataInDbNotZeroUtc() {
    // given a clock
    DeloreanClock clock = getClock();
    // and a timer
    String timerName = "data-in-db-not-zero-z";
    DailyTimer timer = new DailyTimer(timerName, clock);
    // and the time moves past 0 UTC
    clock.forwardDays(1);
    assertTrue(timer.shouldFire(clock.millis()));
    // and an event fires
    timer.fired();
    // and is saved to the db
    assertEquals(1, repo.count());
    // and fire time is set to next 0 UTC
    assertFalse(timer.shouldFire(clock.millis()));

    // when the app is down for say an hour
    timer = null;
    clock.forwardHours(1);
    // and the app is brought back up
    timer = new DailyTimer(timerName, clock);

    // then we should still not fire
    assertFalse(timer.shouldFire(clock.millis()));
    // and after another day we should
    clock.forwardDays(1);
    assertTrue(timer.shouldFire(clock.millis()));
  }

  @Test
  public void testFreshStart() {
    // given the initialized clock
    DeloreanClock clock = getClock();

    // when
    DailyTimer timer = new DailyTimer("test-timer", clock);

    // then the fire time should be in the future next 0 UTC
    assertFalse(timer.shouldFire(clock.millis()));
    // and if time elapses 24 hours
    clock.moveTimeForward(TimeUnit.DAYS.toMillis(1));
    // we should fire
    assertTrue(timer.shouldFire(clock.millis()));
  }

  private DeloreanClock getClock() {
    DeloreanClock clock = new DeloreanClock();
    // and some random static init time
    long initialTime = referenceTime();
    clock.setInitialTime(initialTime);
    return clock;
  }

  private long referenceTime() {
    return 1548361839597L; // ~ 24 Jan 2019 20:30 UTC
  }


  @Test
  public void testAppIsDownWhenZeroUtcPasses() {
    // given the initialized clock
    DeloreanClock clock = getClock();
    // and the time is not 0 UTC
    assertFalse(isZeroUtc(clock.millis()));
    // and
    String timerName = "app-is-down-for-zero-Z-timer";

    // and next fire time is set for the next 0 UTC
    DailyTimer timer = new DailyTimer(timerName, clock);
    assertFalse(timer.shouldFire(clock.millis()));

    // and we pass 0 UTC and successfully save a fire time
    clock.forwardDays(1);
    if (timer.shouldFire(clock.millis())) {
      timer.fired();
    }
    assertEquals(1, repo.count());

    // when timer is null to simulate an outage
    timer = null;
    // and time moves past 0 UTC
    clock.forwardDays(1);
    // and the app comes up
    timer = new DailyTimer(timerName, clock);

    assertEquals(1, repo.count());

    // then the timer should fire on the next data point
    assertTrue(timer.shouldFire(clock.millis()));
    timer.fired();
    // and then be set to the next 0 UTC
    assertFalse(timer.shouldFire(clock.millis()));
    // and since we fired twice there should be two entries
    assertEquals(2, repo.count());
  }

  private boolean isZeroUtc(long milliseconds) {
    Calendar cal = Calendar.getInstance();
    cal.setTimeZone(TimeZone.getTimeZone("GMT"));
    cal.setTimeInMillis(milliseconds);
    return cal.get(Calendar.HOUR_OF_DAY) == 0
        && cal.get(Calendar.MINUTE) == 0
        && cal.get(Calendar.SECOND) == 0
        && cal.get(Calendar.MILLISECOND) == 0;
  }
}
