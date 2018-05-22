package com.frobro.bcindex.web.service.query;

import com.frobro.bcindex.web.model.api.ApiResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.frobro.bcindex.web.service.query.CsvTimeQuery.*;

class CachePopulator implements Populatable {
  private final ApiResponse usd;
  private final ApiResponse btc;

  public CachePopulator(ApiResponse usd, ApiResponse btc) {
    this.usd = usd;
    this.btc = btc;
  }

  @Override
  public Populatable populate(ResultSet result, int size) throws SQLException {

    for (int i=1; i<=size; i++) {
      long time = result.getLong(getTimeCol(i));
      usd.addData(result.getDouble(getUsdCol(i)), time);
      btc.addData(result.getDouble(getBtcCol(i)), time);
    }

    usd.calcAndFormatData();
    btc.calcAndFormatData();
    return this;
  }
}