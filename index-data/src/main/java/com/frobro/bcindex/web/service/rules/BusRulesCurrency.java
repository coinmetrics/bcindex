package com.frobro.bcindex.web.service.rules;

import com.frobro.bcindex.web.constants.StaticValues;

public class BusRulesCurrency extends BusRulesSector {
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
}
