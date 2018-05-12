package com.frobro.bcindex.web.service.cache;

import static com.frobro.bcindex.web.model.api.TimeFrame.*;

import com.frobro.bcindex.core.service.BletchDate;
import com.frobro.bcindex.web.model.api.IndexType;
import com.frobro.bcindex.web.model.api.TimeFrame;
import com.frobro.bcindex.web.service.query.IndexUpdate;
import com.frobro.bcindex.web.service.time.TimeService;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class ExpirationTest {

    @Test
    public void testMaxExpiration() {
        // run once at each with max id less then threshhold
        runAboveAndBelowThresh(DAILY.getNumDataPoints()-1, HOURLY.expireDurationMillis());
        runAboveAndBelowThresh(WEEKLY.getNumDataPoints()-1, DAILY.expireDurationMillis());
        runAboveAndBelowThresh(MONTHLY.getNumDataPoints()-1, MONTHLY.expireDurationMillis());
        runAboveAndBelowThresh(MONTHLY.getNumDataPoints()+1,
                                TimeUnit.MINUTES.toMillis(BletchDate.MIN_IN_DAY));
    }

    private void runAboveAndBelowThresh(long maxId, long timeThresh) {
        assertTrue(runExpire(maxId, timeThresh+1));
        assertFalse(runExpire(maxId, timeThresh));
        assertFalse(runExpire(maxId, timeThresh-1));
    }

    private boolean runExpire(long maxId, long timeMillis) {
        // given expiration for max time frame
        Expiration exp
            = new Expiration(IndexType.ODD_INDEX, TimeFrame.MAX);

        long t0 = TimeService.currentTimeMillis();
        exp.updateLastTime(t0);

        // and
        IndexUpdate firstThresh = new IndexUpdate();

        // when
        firstThresh.setMaxBletchId(maxId);
        firstThresh.setTimeStamp(t0 + timeMillis);

        // then
        return exp.isExpired(firstThresh);
    }

    @Test
    public void testDailyExpire() {
        // given initial time
        long t0 = TimeService.currentTimeMillis();
        // and expiration set to initial time
        Expiration expiration = new Expiration(IndexType.ODD_INDEX, DAILY);
        expiration.updateLastTime(t0);
        // and an update with some dummy data
        IndexUpdate update = new IndexUpdate();
        update.setBtcPrice(1.0).setMaxBletchId(1)
            .setUsdPrice(2.0);

        // when
        long expired = t0 + DAILY.expireDurationMillis() + 10;
        update.setTimeStamp(expired);

        // then
        assertTrue(expiration.isExpired(update));
    }
}
