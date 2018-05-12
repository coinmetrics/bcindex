package com.frobro.bcindex.web.service.publish;

import com.frobro.bcindex.core.db.model.WeightApi;
import com.frobro.bcindex.core.db.service.BletchDate;
import com.frobro.bcindex.web.testframework.MockDailyWeightPubService;
import com.frobro.bcindex.web.testframework.TestClock;
import org.junit.Test;

import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

public class DailyPublishTest {

  @Test
  public void shouldNotPublishUnlessMore24HoursPass() {
    // given
    TestClock clock = getUtcClock();

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
  }

  private TestClock getUtcClock() {
    TestClock clock = new TestClock(ZoneOffset.UTC);
    clock.setInitialTime(BletchDate.getZeroUtcToday());
    return clock;
  }

  @Test
  public void randomTest() {
    // given a time duration
    int numHours = dayToHours(8);

    // and
    TestClock clock = getUtcClock();
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
