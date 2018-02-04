package com.frobro.bcindex.web.service.rules;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.constants.StaticValues;
import com.frobro.bcindex.web.model.Ticker;

import java.util.HashMap;
import java.util.Map;

public class BusRulesTotal extends BusinessRules {
  private static final BcLog log = BcLog.getLogger(BusRulesTotal.class);
  protected static Map<String,Ticker> tickers;

  public BusRulesTotal() {
    if (shouldPopulate(tickers)) {
      populate();
      logMultipliers();
    }
  }

  private void populate() {
    tickers = new HashMap<>();
    populateValuesFromFile(tickers, StaticValues.MKT_CAP_FILE_TOTAL);
  }

  private void logMultipliers() {
    log.debug("total index multipliers");
    logValues(tickers);
  }

  @Override
  protected Map<String, Ticker> getTickers() {
    return tickers;
  }

  @Override
  public double getDivisor() {
    return StaticValues.DIVISOR_TOTAL;
  }

  @Override
  public double getDivisorEven() {
    return StaticValues.DIVISOR_EVEN_TOTAL;
  }

  @Override
  public String indexName() {
    return StaticValues.TOTAL_INDEX_NAME;
  }
}
