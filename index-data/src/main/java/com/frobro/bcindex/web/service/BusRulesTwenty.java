package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.model.Ticker;

import java.util.*;

/**
 * Created by rise on 6/11/17.
 */
public class BusRulesTwenty extends BusinessRules {
  private static final BcLog log = BcLog.getLogger(BusRulesTwenty.class);
  private static final double DIVISOR_20 = 8286853.874;
  private static final double DIVISOR_EVEN_20 = 7275609.532;
  private static final String MKT_CAP_FILE_20 = "business_rules/20_may_rebal.csv";
  private static Map<String,Ticker> tickersTwenty;

  public BusRulesTwenty() {
    if (shouldPopulate(tickersTwenty)) {
      populate();
      logMultipliers();
    }
  }

  private void populate() {
    tickersTwenty = new HashMap<>();
    populateValuesFromFile(tickersTwenty, MKT_CAP_FILE_20);
  }

  private void logMultipliers() {
    log.debug("20 index multipliers");
    logValues(tickersTwenty);
  }

  @Override
  public double getDivisor() {
    return DIVISOR_20;
  }

  @Override
  public double getDivisorEven() {
    return DIVISOR_EVEN_20;
  }

  public Set<String> getIndexes() {
    return new HashSet<>(tickersTwenty.keySet());
  }

  @Override
  public Optional<Double> getMultipler(String tickerName) {
    return extractMultiplier(getTicker(tickerName, tickersTwenty));
  }

  @Override
  public Optional<Double> getMultiplierEven(String tickerName) {
    return extractMultiplierEven(getTicker(tickerName, tickersTwenty));
  }

}
