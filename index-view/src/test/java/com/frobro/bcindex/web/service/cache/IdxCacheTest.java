package com.frobro.bcindex.web.service.cache;

import com.frobro.bcindex.core.db.service.files.BletchFiles;
import com.frobro.bcindex.web.model.api.*;
import org.h2.jdbc.JdbcResultSet;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class IdxCacheTest {

  private static final long HOUR_INTERVAL_MILLIS = TimeUnit.HOURS.toMillis(1);

  @Test @Ignore
  public void testTenIdxUsd() {
    // given request
    RequestDto req = new RequestDto();
    req.currency = Currency.USD;
    req.timeFrame = TimeFrame.MAX;
    req.index = IndexType.ODD_INDEX;

    // and
    DataCache cache = new DataCache(Mockito.mock(JdbcTemplate.class));
    // cache is populated directly not from db

    // and initial response is correct
    ApiResponse response = cache.respondTo(req);

    // when
    // - new data is received
    // - and a new request is sent

    // then
    // - max values reflect the new data
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

