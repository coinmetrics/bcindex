package com.frobro.bcindex.web.service.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.frobro.bcindex.web.domain.JpaEvenIndex;
import com.frobro.bcindex.web.domain.JpaIndex;
import com.frobro.bcindex.web.model.api.*;
import com.frobro.bcindex.web.service.DbTickerService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * Created by rise on 5/13/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TimeSeriesTest extends DbBaseTest {

  @Test @Ignore("summary is not yet implemented")
  public void testSummary() {
    // given
    int numEntries = 300;
    long now = System.currentTimeMillis();
    double firstPrice = 92.0;
    double price = 100.0;
    double prevPrice = 90.0;
    double high = 110.0;
    double low = 85.0;
    // and
    for (int i=1; i<=numEntries; i++) {
      JpaEvenIndex idx = new JpaEvenIndex();
      if (i == 1) {
        idx.setIndexValueUsd(firstPrice);
      } else if (i == 60) {
        idx.setIndexValueUsd(low);
      } else if (i == 120) {
        idx.setIndexValueUsd(high);
      } else if (i == 240) {
        idx.setIndexValueUsd(prevPrice);
      } else if (i == 300) {
        idx.setIndexValueUsd(price);
      }
      // add a minute, since that is the resolution
      // we will save things in prod
      now += TimeUnit.MINUTES.toMillis(1);
      idx.setTimeStamp(now);
      evenRepo.save(idx);
    }
    // and
    assertEquals(numEntries, evenRepo.count());
    // and
    DbTickerService ser = new DbTickerService();
    ser.setJdbc(jdbc);
    // and
    RequestDto req = new RequestDto();
    req.currency = Currency.USD;
    req.index = IndexType.EVEN;
    req.timeFrame = TimeFrame.HOURLY;

    // when
    ApiResponse response = ser.respond(req);

    // and verify summary
    double change = price - firstPrice;
    double percentChg = change / firstPrice;
    assertEquals(price, response.lastPrice, 0.001);
    assertEquals(prevPrice, response.prevClose, 0.001);
    assertEquals(high, response.high, 0.001);
    assertEquals(low, response.low, 0.001);
    assertEquals(change, response.change, 0.001);
    assertEquals(percentChg, response.percentChange, 0.001);
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
    req.currency = Currency.USD;
    req.index = IndexType.ODD;
    req.timeFrame = timeFrame;

    // when
    ApiResponse response = ser.respond(req);
    double close = oddRepo.findOne(1L).getIndexValueUsd();
    assertEquals(close, response.prevClose, 0.00);
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
    assertEquals(price, response.getLastPrice(), 0.001);

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
