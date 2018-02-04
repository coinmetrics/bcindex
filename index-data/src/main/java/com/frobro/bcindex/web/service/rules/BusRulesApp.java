package com.frobro.bcindex.web.service.rules;

import com.frobro.bcindex.web.constants.StaticValues;

public class BusRulesApp extends BusRulesSector {

  @Override
  public double getDivisor() {
    return StaticValues.DIVISOR_APPLICATION;
  }

  @Override
  public String indexName() {
    return StaticValues.APP_INDEX_NAME;
  }

  @Override
  protected String getFileName() {
    return StaticValues.MKT_CAP_FILE_APPLICATION;
  }
}
