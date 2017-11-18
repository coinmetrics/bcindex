package com.frobro.bcindex.web.service.cache;

import static com.frobro.bcindex.web.model.api.TimeFrame.*;

import com.frobro.bcindex.core.db.service.BletchDate;
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
    public void testExpirationBasedOnMaxId() {
        // run once at each with max id less then threshhold
        runAboveAndBelowThresh(DAILY.getNumDataPoints()-1, HOURLY.getTimeStep());
        runAboveAndBelowThresh(WEEKLY.getNumDataPoints()-1, DAILY.getTimeStep());
        runAboveAndBelowThresh(MONTHLY.getNumDataPoints()-1, MONTHLY.getTimeStep());
        runAboveAndBelowThresh(MONTHLY.getNumDataPoints()+1,  BletchDate.MIN_IN_DAY);
    }

    private void runAboveAndBelowThresh(long maxId, long timeThresh) {
        assertTrue(runExpire(maxId, timeThresh+1));
        assertFalse(runExpire(maxId, timeThresh));
        assertFalse(runExpire(maxId, timeThresh-1));
    }

    private boolean runExpire(long maxId, long timeMinutes) {
        // given
        Expiration exp
            = new Expiration(IndexType.ODD_INDEX, TimeFrame.MAX);

        long t0 = TimeService.currentTimeMillis();
        exp.updateLastTime(t0);

        // and
        IndexUpdate firstThresh = new IndexUpdate();

        // when
        firstThresh.setMaxBletchId(maxId);
        firstThresh.setTimeStamp(t0 + TimeUnit.MINUTES.toMillis(timeMinutes));

        // then
        return exp.isExpired(firstThresh);
    }
}
