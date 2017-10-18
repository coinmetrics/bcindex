package com.frobro.bcindex.web.service.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.frobro.bcindex.core.db.service.BletchDate;
import com.frobro.bcindex.core.db.service.files.BletchFiles;
import com.frobro.bcindex.web.model.api.*;
import com.frobro.bcindex.web.model.api.Currency;
import com.frobro.bcindex.web.service.TestDataProvider;
import com.frobro.bcindex.web.service.query.GroupUpdate;
import com.frobro.bcindex.web.service.time.TimeService;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class IdxCacheTest {

  @Test
  public void testTimeRouting() {
    // given
    RequestDto req = new RequestDto();
    req.currency = Currency.USD;
    req.index = IndexType.ODD_INDEX;
    // and time frame is set
    req.timeFrame = TimeFrame.HOURLY;
    ApiResponse resp = ApiResponse.newResponse(req);
    DataCache cache = new DataCache();

    for (TimeFrame frame : TimeFrame.values()) {
      // and
      resp.timeFrame = frame;
      long t0 = System.currentTimeMillis();
      resp.updateLast(0.0, t0);

      // when a time  PAST the daily time step is set
      long future = t0 + TimeUnit.MINUTES.toMillis(frame.getTimeStep()+1);
      // and a time before the daily time step is set
      long past = t0 + TimeUnit.MINUTES.toMillis(frame.getTimeStep()-1);
      long onTimeStep = t0 + TimeUnit.MINUTES.toMillis(frame.getTimeStep());

      String errMsg = "failed for frame: " + resp.timeFrame;
      // then the time should be elapsed
      assertTrue(errMsg, cache.timeElapsed(resp, future));

      // and before should not be elasped
      assertFalse(errMsg, cache.timeElapsed(resp, past));
      // and on time step should also not be elasped
      assertFalse(errMsg, cache.timeElapsed(resp, onTimeStep));
    }
  }

  @Test
  public void testUpdateInApiResp() {
    // given
    RequestDto req = new RequestDto();
    req.currency = Currency.USD;
    req.timeFrame = TimeFrame.DAILY;
    req.index = IndexType.ODD_INDEX;

    List<Double> expData = new ArrayList<>();
    expData.add(1.0); expData.add(2.0);
    expData.add(3.0); expData.add(4.0);
    expData.add(5.0);
    List<Long> expTimes = Arrays.asList(1L, 2L, 3L, 4L, 5L);
    // and
    ApiResponse resp = ApiResponse.newResponse(req);
    expData.stream().forEach(d -> {resp.addData(d,0L);});
    resp.high = 5.0;
    resp.low = 1.0;
    resp.times = expTimes.stream()
        .map(t -> BletchDate.toDate(t))
        .collect(Collectors.toList());
    resp.calcAndFormatData();

    // when
    double newData = 100.0;
    resp.update(TimeService.currentTimeMillis(), newData);

    expData.add(newData);
    int size = expData.size();
    assertEquals(size-1, resp.data.size());
    assertEquals(expData.subList(1,size),resp.data);
  }

  @Test
  public void testTenIdxUsd() {
    // given request
    RequestDto req = new RequestDto();
    req.currency = Currency.USD;
    req.timeFrame = TimeFrame.DAILY;
    req.index = IndexType.ODD_INDEX;

    // and
    DataCache cache = new DataCache();
    TestDataProvider dataProvider = new TestDataProvider();
    // cache is populated directly not from db
    cache.populateFromDb(dataProvider);

    // and initial response is equal to the
    // data provided
    ApiResponse response = cache.respondTo(req);
    assertEquals(dataProvider.getData(req), response);
    // and a data copy is made to validate against
    // otherwise the prod updates will affect the initial list
    List<Double> initialData = new ArrayList<>(response.data);
    List<String> initialTimes = new ArrayList<>(response.times);

    // and
    GroupUpdate update = new GroupUpdate();

    // when - new data is received
    double pxUsd = 123.00;
    double pxBtc = 1.0304;
    long time = 0;//System.currentTimeMillis();// + TimeUnit.DAYS.toMillis(7);
    update.updateAll(pxUsd, pxBtc, time);
    cache.update(update);

    // and a new request is sent
    ApiResponse updatedResp = cache.respondTo(req);

    // then
    assertTrue(response == updatedResp); // same instance from map
    assertEquals(pxUsd, updatedResp.lastPrice, 0.001);
    // and
    int size = initialData.size();
    assertEquals(size, updatedResp.data.size());
    assertEquals(size, updatedResp.times.size());
    // all data is the same except the 1st data is not in update, and last is
    // not in initial. ex: [1,2,3] + update(8) -> [2,3,8]
    assertEquals(initialData.subList(1,size), updatedResp.data.subList(0, size - 1));
    assertEquals(pxUsd, updatedResp.data.get(size - 1),0.001);
    // and times are correct
    long roundedTime = updatedResp.timeFrame.round(time);
    assertEquals(roundedTime, updatedResp.getLatestTime());
    assertEquals(BletchDate.toDate(roundedTime), updatedResp.times.get(size - 1));
    assertEquals(initialTimes.subList(1, size),updatedResp.times.subList(0, size - 1));
  }

  @Test
  public void bunchOfperumtationsOfAbove() {

  }

  private void populateDataTen(String fileName,
                               DataCache cache) {
    final String delim = ",";
    final int btcPos = 1, usdPos = 2, datePos = 3;
    List<String> lines = BletchFiles.linesToList(fileName);

    long lastDate = 0;
    ResultSet result = Mockito.mock(ResultSet.class);

    for (String line : lines) {
      String[] vals = line.split(delim);

//      idx.setIndexValueBtc(Double.parseDouble(vals[btcPos]));
//      idx.setIndexValueUsd(Double.parseDouble(vals[usdPos]));
//      idx.setTimeStamp(vals[datePos]);
//
//      eth.setIndexValueBtc(Double.parseDouble(vals[btcPos]));
//      eth.setIndexValueUsd(Double.parseDouble(vals[usdPos]));
//      eth.setTimeStamp(vals[datePos]);
    }
  }
}

