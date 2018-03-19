package com.frobro.bcindex.web.service.rules;

import com.frobro.bcindex.web.constants.StaticValues;
import com.frobro.bcindex.web.model.Ticker;

import java.util.HashMap;
import java.util.Map;

public class BusRulesCurrency extends BusRulesSector {
  protected static Map<String,Ticker> tickers;

  public BusRulesCurrency() {
    if (shouldPopulate(tickers)) {
      populate();
      logValues(tickers, indexName());
    }
  }

  private void populate() {
    tickers = new HashMap<>();
    populateValuesFromFile(tickers, StaticValues.MKT_CAP_FILE_CURRENCY);
  }

  @Override
  protected Map<String,Ticker> getTickers() {
    return tickers;
  }

  @Override
  public double getDivisor() {
    return StaticValues.DIVISOR_CURRENCY;
  }

  @Override
  public String indexName() {
    return StaticValues.CURR_INDEX_NAME;
  }

  @Override
  protected String getFileName() {
    return StaticValues.MKT_CAP_FILE_CURRENCY;
  }

  public boolean hasEven() {
    return Boolean.FALSE;
  }
}
