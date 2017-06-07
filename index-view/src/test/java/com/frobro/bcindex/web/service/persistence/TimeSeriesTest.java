package com.frobro.bcindex.web.service.persistence;

import com.frobro.bcindex.web.domain.JpaIndex;
import com.frobro.bcindex.web.model.api.*;
import com.frobro.bcindex.web.service.DbTickerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rise on 5/13/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TimeSeriesTest extends DbBaseTest {

  @Test
  public void testClose() {
    // given
    double price = 100.0;
    TimeFrame timeFrame = TimeFrame.HOURLY;
    int numEntries = 60; // 10* 60 (data pts * min/hour)
    populateDb(numEntries, price);

    DbTickerService ser = new DbTickerService();
    ser.setJdbc(jdbc);
    // and
    RequestDto req = new RequestDto();
    req.currency = Currency.BTC;
    req.index = IndexType.ODD;
    req.timeFrame = timeFrame;

    // when
    ApiResponse response = ser.respond(req);

    double btcClose = oddRepo.findOne(1L).getIndexValueBtc();
    assertEquals(btcClose, response.prevClose, 0.01);
    assertEquals(req.currency, response.currency);
  }

  private void populateDb(int numEntries, double price) {
    long now = System.currentTimeMillis();
    for (int i=1; i<=numEntries; i++) {
      JpaIndex idx = IndexFactory.getNewOdd();
      if (i == numEntries) {
        idx.setIndexValueUsd(price);
      }
      // add a minute, since that is the resolution
      // we will save things in prod
      now += TimeUnit.MINUTES.toMillis(1);
      idx.setTimeStamp(now);
      oddRepo.save(idx);
    }
  }

  @Test
  public void testOddRepoHourly() {
    // given
    double price = 100.0;
    TimeFrame timeFrame = TimeFrame.HOURLY;
    int numEntries = 600; // 10* 60 (data pts * min/hour)
    populateDb(numEntries, price);
    System.out.println("size: " + oddRepo.count());

    // and
    assertEquals(numEntries, oddRepo.count());

    // and
    DbTickerService ser = new DbTickerService();
    ser.setJdbc(jdbc);
    // and
    RequestDto req = new RequestDto();
    req.currency = Currency.USD;
    req.index = IndexType.ODD;
    req.timeFrame = timeFrame;

    // when
    ApiResponse response = ser.respond(req);

    // then verify pass throughs
    assertNotNull(response);
    assertEquals(req.currency, response.currency);
    assertEquals(req.index, response.index);
    assertEquals(req.timeFrame, response.timeFrame);
    assertEquals(req.timeFrame.getTimeStepUnit(),
        response.timeUnit);

    // and verify last price
    assertEquals(price, response.getLastPrice(), 0.01);

    // and verify time series
    int entries = timeFrame.getModNum();
    // if we didn't populate an entire time frame
    // i.e. 600 minutes is no enough minutes to fill
    // a day (1440), just use the num entries
    if (numEntries < timeFrame.getNumDataPoints()) {
      entries = numEntries/timeFrame.getTimeStep();
    }
    assertEquals(entries, response.data.size());
    assertEquals(entries, response.times.size());
  }
}
