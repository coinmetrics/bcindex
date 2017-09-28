package com.frobro.bcindex.web.service.cache;

import com.frobro.bcindex.web.model.api.*;
import com.frobro.bcindex.web.service.query.CsvTimeQuery;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.Map;

public class DataCache {
  private final Map<String,ApiResponse> apiMap = new HashMap<>();

  // 10 max
  private ApiResponse tenUsdMax = getTenUsdDto(TimeFrame.MAX);
  private ApiResponse tenBtcMax = getTenBtcDto(TimeFrame.MAX);
  // 10 monthly
  private ApiResponse tenMonthUsd = getTenUsdDto(TimeFrame.MONTHLY);
  private ApiResponse tenMonthBtc = getTenBtcDto(TimeFrame.MONTHLY);
  // 10 weekly
  private ApiResponse tenWkUsd = getTenUsdDto(TimeFrame.WEEKLY);
  private ApiResponse tenWkBtc = getTenBtcDto(TimeFrame.WEEKLY);
  // 10 daily
  private ApiResponse tenDlyUsd = getTenUsdDto(TimeFrame.DAILY);
  private ApiResponse tenDlyBtc = getTenBtcDto(TimeFrame.DAILY);
  // 10 hourly
  private ApiResponse tenHourUsd = getTenUsdDto(TimeFrame.HOURLY);
  private ApiResponse tenHourBtc = getTenBtcDto(TimeFrame.HOURLY);
  // ---------
  // 10 even usd
  // 10 even btc
  // 20 usd
  // 20 btc
  // 20 even usd
  // 20 even btc
  // eth usd
  // eth btc

  private final JdbcTemplate jdbc;

  private static ApiResponse getTenUsdDto(TimeFrame frame) {
    RequestDto dto = new RequestDto();
    dto.currency = Currency.USD;
    dto.timeFrame = frame;
    dto.index = IndexType.ODD_INDEX;
    return ApiResponse.newResponse(dto);
  }

  private static ApiResponse getTenBtcDto(TimeFrame frame) {
    RequestDto dto = new RequestDto();
    dto.currency = Currency.BTC;
    dto.timeFrame = frame;
    dto.index = IndexType.ODD_INDEX;
    return ApiResponse.newResponse(dto);
  }

  public DataCache(JdbcTemplate jdbc) {
    this.jdbc = jdbc;
  }

  public ApiResponse respondTo(RequestDto req) {

    return null;
  }

  private void populate() {
    // for every index
    //    for every time frame
    //        populate usd and btc responses
    populate(IndexType.ODD_INDEX, TimeFrame.MONTHLY);
  }

  private void populate(IndexType index, TimeFrame timeFrame) {
    CsvTimeQuery query = new CsvTimeQuery(jdbc, timeFrame.getTimeStep());
    // create usd
    ApiResponse usd = ApiResponse.newResponse(index, timeFrame, Currency.USD);
    // create btc
    ApiResponse btc = ApiResponse.newResponse(index, timeFrame, Currency.BTC);
    // make the db call and populate
    query.getCacheContent(index.name(), timeFrame, usd, btc);
    // create usd string and put
    apiMap.put(createKey(usd.index, usd.timeFrame, usd.currency),usd);
    // create btc string and put
    apiMap.put(createKey(btc.index, btc.timeFrame, btc.currency),btc);
  }

  private String createKey(IndexType index, TimeFrame frame, Currency currency) {
    return index.name() + "." + frame.name() + "." + currency;
  }

  private double formatUsd(double d) {
    return Currency.USD.format(d);
  }

  private double formatBtc(double d) {
    return Currency.BTC.format(d);
  }
}
