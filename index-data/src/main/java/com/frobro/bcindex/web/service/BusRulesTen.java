package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.constants.StaticValues;
import com.frobro.bcindex.web.model.BletchleyData;
import com.frobro.bcindex.web.model.Ticker;

import java.util.*;

/**
 * Created by rise on 6/11/17.
 */
public class BusRulesTen extends BusinessRules {
  private static final BcLog log = BcLog.getLogger(BusRulesTen.class);
  protected static Map<String,Ticker> tickers;

  public BusRulesTen() {
    if (shouldPopulate(tickers)) {
      populate();
      logMultipliers();
    }
  }

  private void populate() {
    tickers = new HashMap<>();
    populateValuesFromFile(tickers, StaticValues.MKT_CAP_FILE);
    Ticker btcTicker = getBtcTicker();
    tickers.put(btcTicker.getName(), btcTicker);
  }

  private Ticker getBtcTicker() {
    Ticker btcTicker = new Ticker(BletchleyData.getUsdBtcTicker());
    btcTicker.setEvenMultiplier(0).setMultiplier(0);

    return btcTicker;
  }

  private void logMultipliers() {
    log.debug("10 index multipliers");
    logValues(tickers);
  }

  public double getConstant() {
    return StaticValues.CONSTANT_TEN;
  }

  public double getConstantEven() {
    return StaticValues.CONSTANT_EVEN_TEN;
  }

  @Override
  protected Map<String,Ticker> getTickers() {
    return tickers;
  }

  @Override
  public double getDivisor() {
    return StaticValues.DIVISOR_TEN;
  }

  @Override
  public double getDivisorEven() {
    return StaticValues.DIVISOR_EVEN_TEN;
  }
}
