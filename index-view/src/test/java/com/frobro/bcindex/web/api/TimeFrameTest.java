package com.frobro.bcindex.web.api;

import com.frobro.bcindex.web.model.api.TimeFrame;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class TimeFrameTest {

    @Test
    public void testTimeFrameExpire() {
        // hr = 1 minute
        // day = 24 minutes
        // week = 1 hour
        // month = 12 hour
        long lessHr = TimeUnit.SECONDS.toMillis(40);
        long overHr = TimeUnit.MINUTES.toMillis(2);
        long overDay = TimeUnit.MINUTES.toMillis(45);
        long overWk = TimeUnit.HOURS.toMillis(2);
        long overMth = TimeUnit.DAYS.toMillis(1);

        assertFalse(TimeFrame.HOURLY.timeElapsed(lessHr));
        assertTrue(TimeFrame.HOURLY.timeElapsed(overHr));

        assertFalse(TimeFrame.DAILY.timeElapsed(lessHr));
        assertTrue(TimeFrame.DAILY.timeElapsed(overDay));

        assertFalse(TimeFrame.WEEKLY.timeElapsed(lessHr));
        assertTrue(TimeFrame.WEEKLY.timeElapsed(overWk));

        assertFalse(TimeFrame.MONTHLY.timeElapsed(lessHr));
        assertTrue(TimeFrame.MONTHLY.timeElapsed(overMth));
    }
}
