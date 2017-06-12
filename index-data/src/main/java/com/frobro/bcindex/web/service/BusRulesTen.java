package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.model.BletchleyData;
import com.frobro.bcindex.web.model.Ticker;

import java.util.*;

/**
 * Created by rise on 6/11/17.
 */
public class BusRulesTen extends BusinessRules {
  private static final BcLog log = BcLog.getLogger(BusRulesTen.class);
  private static final double DIVISOR = 21901918.35;
  private static final double DIVISOR_EVEN = 10964215.29;
  private static final double CONSTANT = 16333862;
  private static final double CONSTANT_EVEN = 3022990.404;
  private static final String MKT_CAP_FILE = "business_rules/May_rebal.csv";
  private static Map<String,Ticker> tickersTen;

  public BusRulesTen() {
    if (shouldPopulate(tickersTen)) {
      populate();
      logMultipliers();
    }
  }

  private void populate() {
    tickersTen = new HashMap<>();
    populateValuesFromFile(tickersTen, MKT_CAP_FILE);
    Ticker btcTicker = getBtcTicker();
    tickersTen.put(btcTicker.getName(), btcTicker);
  }

  private Ticker getBtcTicker() {
    Ticker btcTicker = new Ticker(BletchleyData.getUsdBtcTicker());
    btcTicker.setEvenMultiplier(0).setMultiplier(0);

    return btcTicker;
  }

  private void logMultipliers() {
    log.debug("10 index multipliers");
    logValues(tickersTen);
  }

  public Set<String> getIndexes() {
    return new HashSet<>(tickersTen.keySet());
  }

  @Override
  public Optional<Double> getMultipler(String tickerName) {
    return extractMultiplier(getTicker(tickerName, tickersTen));
  }

  @Override
  public Optional<Double> getMultiplierEven(String tickerName) {
    return extractMultiplierEven(getTicker(tickerName, tickersTen));
  }

  public double getConstant() {
    return CONSTANT;
  }

  public double getConstantEven() {
    return CONSTANT_EVEN;
  }

  @Override
  public double getDivisor() {
    return DIVISOR;
  }

  @Override
  public double getDivisorEven() {
    return DIVISOR_EVEN;
  }
}
