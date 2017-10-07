package com.frobro.bcindex.web.service.cache;

import static org.junit.Assert.assertEquals;
import com.frobro.bcindex.core.db.service.files.BletchFiles;
import com.frobro.bcindex.web.model.api.*;
import com.frobro.bcindex.web.service.TestDataProvider;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class IdxCacheTest {

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

    // and initial response is correct
    ApiResponse response = cache.respondTo(req);

    // when
    // - new data is received
    // - and a new request is sent

    // then
    // - max values reflect the new data
    assertEquals(dataProvider.getData(req), response);
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

