package com.frobro.bcindex.web.service.time;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

/**
 * Unit test for simple App.
 */
public class TestClock extends BletchClock {

    private Long startTime;
    private Long offset;

    public TestClock setClock() {
        startTime = System.currentTimeMillis();
        offset = 0L;
        return this;
    }

    public TestClock forwardMinutes(int numMin) {
        return forwardTime(numMin, MINUTES);
    }

    public TestClock forwardHours(int numHrs) {
        return forwardTime(numHrs, HOURS);
    }

    public TestClock forwardDays(int numDays) {
        return forwardTime(numDays, DAYS);
    }

    private TestClock forwardTime(int num, TimeUnit unit) {
        offset += unit.toMillis(num);
        return this;
    }

    @Override
    public long getTimeEpochMillis() {
        return startTime + offset;
    }
}
