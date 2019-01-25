package com.frobro.bcindex.web.service.publish;

import com.frobro.bcindex.core.model.WeightApi;
import com.frobro.bcindex.web.service.DailyTimer;
import com.frobro.bcindex.web.service.persistence.DailyFireTimes;
import com.frobro.bcindex.web.service.persistence.DailyTimerRepo;
import com.frobro.bcindex.web.testframework.MockDailyWeightPubService;
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

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DailyPublishTest {

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
  public void shouldNotPublishUnlessMore24HoursPass() {
    // given
    DeloreanClock clock = getUtcClock();

    // and time is set to 1am UTC
    clock.moveTimeForward(HOURS.toMillis(1));
    DailyWeightPubService pubService = new MockDailyWeightPubService(clock);

    // and that time is set on the incoming data
    WeightApi data = new WeightApi();
    data.setTime(clock.millis());

    // when publish is called
    pubService.publish(data);

    // and nothing should happen. ie publish count == 0
    assertEquals(0, pubService.getNumTimesPublished());

    // when time is moved forward > 24 hours
    clock.moveTimeForward(HOURS.toMillis(24)
        + SECONDS.toMillis(5));

    // and new time is set on incoming data
    data.setTime(clock.millis());
    // and publish is called
    pubService.publish(data);

    // then expect one publish event to occur
    assertEquals(1, pubService.getNumTimesPublished());
    // and time to be reset so another publish will not happen
    clock.forwardHours(3);
    data.setTime(clock.millis());
    pubService.publish(data);
    // expect no publish. publish time has been reset to 0 UTC today
    assertEquals(1, pubService.getNumTimesPublished());
  }

  private DeloreanClock getUtcClock() {
    return DeloreanClock.getUtcClock();
  }

  @Test
  public void randomTest() {
    // given a time duration of 8 days
    int numHours = dayToHours(8);

    // and
    DeloreanClock clock = getUtcClock();
    DailyWeightPubService pubService = new MockDailyWeightPubService(clock);
    WeightApi data = new WeightApi();
    // and
    data.setTime(clock.millis());
    // and publish is called
    pubService.publish(data);

    long publishCount = 0;

    // when move time forward 1 hour at a time
    for (int i=0; i<numHours; i++) {
      pubService.publish(data);

      // then if day elapses expect publish count to ++
      if (dayElapsed(i)) {
        publishCount++;
      }

      // and
      assertEquals(publishCount,pubService.getNumTimesPublished());

      // move time forward an hour for next iteration
      clock.forwardHours(1);
      data.setTime(clock.millis());
    }
  }

  @Test
  public void existingFireTimesInDb() {
    DeloreanClock clock = getUtcClock();

    DailyTimer timer = new DailyTimer(DailyWeightPubService.getTimerName(),clock);
    clock.forwardHours(25);
    if (timer.shouldFire(clock.millis())) {
      timer.fired();
    }

    clock.forwardDays(1);
    if (timer.shouldFire(clock.millis())) {
      timer.fired();
    }

    assertEquals(2L, timer.getTimesFired());
    assertEquals(2L, repo.count());

    DailyWeightPubService pubService = new MockDailyWeightPubService(clock);

    WeightApi data = new WeightApi();
    clock.forwardHours(5);
    data.setTime(clock.millis());

    // when
    pubService.publish(data);
    // and no publish happened because the time is not passed 0 UTC
    assertEquals(0, pubService.getNumTimesPublished());
    // and move time forward past 0 UTC
    clock.forwardHours(24);
    // set the new time on the data
    data.setTime(clock.millis());
    // and call publish again
    pubService.publish(data);

    // then expect one publish
    assertEquals(1, pubService.getNumTimesPublished());
  }

  private boolean dayElapsed(int numHours) {
    if (numHours == 0) {
      return false;
    }
     return numHours % dayToHours(1) == 0;
  }

  private int dayToHours(int numDays) {
    return (int)TimeUnit.DAYS.toHours(numDays);
  }
}
