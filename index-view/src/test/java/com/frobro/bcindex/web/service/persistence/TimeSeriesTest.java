package com.frobro.bcindex.web.service.persistence;

import com.frobro.bcindex.web.model.api.*;
import com.frobro.bcindex.web.service.DbTickerService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rise on 5/13/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
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
    req.index = IndexType.ODD_INDEX;
    req.timeFrame = timeFrame;

    // when
    ApiResponse response = ser.getData(req);

    verifyPassThroughFields(req, response);
    double btcClose = oddRepo.findById(1L).get().getIndexValueBtc();
    assertEquals(btcClose, response.prevClose, 0.01);
    assertEquals(req.currency, response.currency);
  }

  @Test
  public void testOddRepoHourly() {
    // given
    double price = 100.0;
    TimeFrame timeFrame = TimeFrame.HOURLY;
    int numEntries = 600; // 10* 60 (data pts * min/hour)
    populateDb(numEntries, price);

    // and
    assertEquals(numEntries, oddRepo.count());

    // and
    DbTickerService ser = new DbTickerService();
    ser.setJdbc(jdbc);
    // and
    RequestDto req = new RequestDto();
    req.currency = Currency.USD;
    req.index = IndexType.ODD_INDEX;
    req.timeFrame = timeFrame;

    // when
    ApiResponse response = ser.getData(req);

    verifyPassThroughFields(req, response);

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

  private void verifyPassThroughFields(RequestDto req, ApiResponse response) {
    assertNotNull(response);
    assertEquals(req.currency, response.currency);
    assertEquals(req.index, response.index);
    assertEquals(req.timeFrame, response.timeFrame);
    assertEquals(req.timeFrame.getTimeStepUnit(),
        response.timeUnit);
  }
}
