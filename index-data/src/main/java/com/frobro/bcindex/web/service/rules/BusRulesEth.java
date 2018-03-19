package com.frobro.bcindex.web.service.rules;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.constants.StaticValues;
import com.frobro.bcindex.web.model.Ticker;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rise on 7/16/17.
 */
public class BusRulesEth extends HasEvenIndexRules {
  private static final BcLog log = BcLog.getLogger(BusRulesEth.class);
  protected static Map<String,Ticker> tickers;

  public BusRulesEth() {
    if (shouldPopulate(tickers)) {
      populate();
      logValues(tickers, indexName());
    }
  }

  private void populate() {
    tickers = new HashMap<>();
    populateValuesFromFile(tickers, StaticValues.MKT_CAP_FILE_EHTER);
  }

  @Override
  protected Map<String,Ticker> getTickers() {
    return tickers;
  }

  @Override
  public double getDivisor() {
    return StaticValues.DIVISOR_ETHER;
  }

  @Override
  public double getDivisorEven() {
    return StaticValues.DIVISOR_EVEN_ETHER;
  }

  @Override
  public String indexName() {
    return StaticValues.ETH_INDEX_NAME;
  }
}
