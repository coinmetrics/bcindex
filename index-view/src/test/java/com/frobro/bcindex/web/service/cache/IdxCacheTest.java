package com.frobro.bcindex.web.service.cache;

import static com.frobro.bcindex.web.service.cache.DataNamer.createName;
import static org.junit.Assert.*;

import com.frobro.bcindex.core.service.BletchDate;
import com.frobro.bcindex.web.framework.ExpirationPopulator;
import com.frobro.bcindex.web.model.api.*;
import com.frobro.bcindex.web.model.api.Currency;
import com.frobro.bcindex.web.service.DataProvider;
import com.frobro.bcindex.web.service.TestDataProvider;
import com.frobro.bcindex.web.service.TimeSeriesService;
import com.frobro.bcindex.web.service.query.GroupUpdate;
import com.frobro.bcindex.web.service.time.TimeService;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class IdxCacheTest {

  @Test
  public void testMaxTimeFrameInCache() {
    // given
    RequestDto req = new RequestDto();
    req.currency = Currency.USD;
    req.index = IndexType.ODD_INDEX;

    // and time frame is set
    req.timeFrame = TimeFrame.HOURLY;
    ApiResponse resp = ApiResponse.newResponse(req);
    DataCache cache = new DataCache();

    // and
    cacheIsPopulated(cache);

  }

  @Test
  public void testSimpleRouting() {
    // given
    RequestDto req = new RequestDto();
    req.currency = Currency.USD;
    req.index = IndexType.ODD_INDEX;

    // and time frame is set
    req.timeFrame = TimeFrame.HOURLY;
    ApiResponse resp = ApiResponse.newResponse(req);
    DataCache cache = new DataCache();

    // and
    cacheIsPopulated(cache);

    // when we request api data from the cache
    ApiResponse cacheResponse = cache.respondTo(req);

    // then
    assertEquals(req.index, cacheResponse.index);
    assertEquals(req.timeFrame, cacheResponse.timeFrame);
    assertEquals(req.currency, cacheResponse.currency);
  }

  private DataProvider cacheIsPopulated(DataCache cache) {
    TestDataProvider dataProvider = new TestDataProvider();
    cache.populateFromDb(dataProvider);
    return dataProvider;
  }

  @Test
  public void testExpiresAreUpdated() {
    // given
    RequestDto req = new RequestDto();
    req.index = IndexType.ODD_INDEX;
    // and time frame is set
    req.timeFrame = TimeFrame.DAILY;
    req.currency = Currency.USD;
    DataCache cache = new DataCache();
    // and
    cacheIsPopulated(cache);

    CacheUpdateMgr mgr = new CacheUpdateMgr(cache, new TimeSeriesService());
    GroupUpdate update = new GroupUpdate();

    // and expirations are populated
    long t0 = TimeService.currentTimeMillis();
    mgr.set(ExpirationPopulator.createExpirations(t0));

    long pastExpireTime = t0 + TimeUnit.MINUTES.toMillis(req.timeFrame.getTimeStep() + 1);

    // and
    double newPxUsd = 123.00;
    double newPxBtc = 1.0304;
    long maxBletchId = 500000;
    // update only the 10 index
    update.updateTen(newPxUsd, newPxBtc, pastExpireTime, maxBletchId);

    // and - new data is received
    Set<String> expiredFuture = mgr.update(update);
    assertEquals(createExpectInclusive(req), expiredFuture);

    // and first time and px is saved
    ApiResponse data = cache.respondTo(req);
    long firstTime = data.getFirstTime();
    double firstPx = data.getFirstPrice();
    long expectedSize = data.times.size();

    // and
    double usdRej = 130.0;
    double btcRej = 1.08;
    long maxIdRej = 600000;

    // when
    long beforeExpireTime = t0 + TimeUnit.MINUTES.toMillis(1) - 1;
    // and
    update.updateTen(usdRej, btcRej, beforeExpireTime, maxIdRej);
    Set<String> rejectedUpdate = mgr.update(update);

    // then
    assertEquals(Collections.emptySet(), rejectedUpdate);
    ApiResponse resp = cache.respondTo(req);
    // and last time is equals beforeExpireTime because it was overwritten
    // but the oldest time should not be removed
    assertEquals(req.timeFrame.round(beforeExpireTime), resp.getLatestTime());
    assertEquals(firstTime, resp.getFirstTime());
    // and last px is equal to usdRej
    assertEquals(usdRej, resp.getLastPrice(), 0.001);
    // and the first px is not removed
    assertEquals(firstPx, resp.getFirstPrice(), 0.001);
    // and the data has the same amount of elements
    assertEquals(expectedSize, resp.data.size());
    assertEquals(expectedSize, resp.times.size());
  }

  @Test
  public void testSimpleExpire() {
    testExpire(TimeFrame.DAILY);
    testExpire(TimeFrame.HOURLY);
    testExpire(TimeFrame.WEEKLY);
    testExpire(TimeFrame.MONTHLY);
  }

  private void testExpire(TimeFrame timeFrame) {
    // given
    RequestDto req = new RequestDto();
    req.index = IndexType.ODD_INDEX;
    // and time frame is set
    req.timeFrame = timeFrame;
    req.currency = Currency.USD;
    DataCache cache = new DataCache();
    // and
    cacheIsPopulated(cache);

    // and initial prices are saved
    final Map<TimeFrame, Double> originalFstPx = new HashMap<>();
    final Map<TimeFrame, Double> originalLastPx = new HashMap<>();

    req.timeFrame.getLargerTimeFrames().stream().forEach(t -> {
      RequestDto d = req.copy();
      d.timeFrame = t;
      ApiResponse data = cache.respondTo(d);
      originalFstPx.put(t, data.getFirstPrice());
      originalLastPx.put(t, data.getLastPrice());
    });

    // and
    CacheUpdateMgr mgr = new CacheUpdateMgr(cache, new TimeSeriesService());
    GroupUpdate update = new GroupUpdate();

    // and expirations are populated
    long t0 = TimeService.currentTimeMillis();
    mgr.set(ExpirationPopulator.createExpirations(t0));

    // and initial time is set to past expiration
    long future = t0 + TimeUnit.MINUTES.toMillis(req.timeFrame.getTimeStep() + 1);

    // and
    double newPxUsd = 123.00;
    double newPxBtc = 1.0304;
    long maxBletchId = 500000;
    // update only the 10 index
    update.updateTen(newPxUsd, newPxBtc, future, maxBletchId);

    // when - new data is received
    Set<String> expiredFuture = mgr.update(update);

    // then
    // expect hourly and daily to expire
    assertEquals(createExpectInclusive(req), expiredFuture);
    // and the last price is retrieved
    ApiResponse resp = cache.respondTo(req);
    assertEquals(newPxUsd, resp.lastPrice, 0.001);
    assertEquals(newPxUsd, resp.data.get(resp.data.size() - 1), 0.001);

    String formattedTime = BletchDate.toDate(req.timeFrame.round(future));
    // and latest time has the new time
    assertEquals(formattedTime,
        BletchDate.toDate(resp.getLatestTime()));
    // and the last time in the time array has the new time
    assertEquals(formattedTime, resp.times.get(resp.times.size() - 1));

    // and the other time frames are not updated
    req.timeFrame.getLargerTimeFrames().stream().forEach(t -> {
      RequestDto d = req.copy();
      d.timeFrame = t;
      // first price is the same
      assertEquals(originalFstPx.get(t), cache.respondTo(d).getFirstPrice(), 0.001);
      // but last price needs to change to keep all frames in sync
      assertEquals(newPxUsd, cache.respondTo(d).getLastPrice(), 0.001);
    });
  }

  private Set<String> createExpectInclusive(RequestDto req) {
    Set<String> expected = new HashSet<>();

    // include current time frame
    expected.add(createName(req.index, req.timeFrame));

    return createExpect(expected, req);
  }

  private Set<String> createExpect(RequestDto req) {
    return createExpect(new HashSet<>(), req);
  }

  private Set<String> createExpect(Set<String> set, RequestDto req) {
    req.timeFrame.getSmallerTimeFrames().stream().forEach(t -> {
      set.add(createName(req.index, t));
    });
    return set;
  }

  @Test
  public void testUpdateInApiResponse() {
    // given
    RequestDto req = new RequestDto();
    req.currency = Currency.USD;
    req.timeFrame = TimeFrame.DAILY;
    req.index = IndexType.ODD_INDEX;

    List<Double> expData = new ArrayList<>();
    expData.add(1.0);
    expData.add(2.0);
    expData.add(3.0);
    expData.add(4.0);
    expData.add(5.0);
    List<Long> expTimes = Arrays.asList(1L, 2L, 3L, 4L, 5L);
    // and
    ApiResponse resp = ApiResponse.newResponse(req);
    expData.stream().forEach(d -> {
      resp.addData(d, 0L);
    });
    resp.high = 5.0;
    resp.low = 1.0;
    resp.times = expTimes.stream()
        .map(t -> BletchDate.toDate(t))
        .collect(Collectors.toList());
    resp.calcAndFormatData();

    // when
    double newData = 100.0;
    resp.addNewAndRemoveLast(TimeService.currentTimeMillis(), newData);

    expData.add(newData);
    int size = expData.size();
    assertEquals(size - 1, resp.data.size());
    assertEquals(expData.subList(1, size), resp.data);
  }
}

