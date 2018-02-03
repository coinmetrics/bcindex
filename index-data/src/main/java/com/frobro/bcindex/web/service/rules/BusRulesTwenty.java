package com.frobro.bcindex.web.service.rules;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.constants.StaticValues;
import com.frobro.bcindex.web.model.Ticker;

import java.util.*;

/**
 * Created by rise on 6/11/17.
 */
public class BusRulesTwenty extends BusinessRules {
  private static final BcLog log = BcLog.getLogger(BusRulesTwenty.class);
  protected static Map<String,Ticker> tickers;

  public BusRulesTwenty() {
    if (shouldPopulate(tickers)) {
      populate();
      logMultipliers();
    }
  }

  private void populate() {
    tickers = new HashMap<>();
    populateValuesFromFile(tickers, StaticValues.MKT_CAP_FILE_20);
  }

  private void logMultipliers() {
    log.debug("20 index multipliers");
    logValues(tickers);
  }

  @Override
  protected Map<String,Ticker> getTickers() {
    return tickers;
  }

  @Override
  public double getDivisor() {
    return StaticValues.DIVISOR_20;
  }

  @Override
  public double getDivisorEven() {
    return StaticValues.DIVISOR_EVEN_20;
  }
}
