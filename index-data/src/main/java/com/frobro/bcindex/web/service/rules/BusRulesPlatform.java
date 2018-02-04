package com.frobro.bcindex.web.service.rules;

import com.frobro.bcindex.web.constants.StaticValues;

public class BusRulesPlatform extends BusRulesSector {
  @Override
  public double getDivisor() {
    return StaticValues.DIVISOR_PLATFORM;
  }

  @Override
  public String indexName() {
    return StaticValues.PLATFORM_INDEX_NAME;
  }

  @Override
  protected String getFileName() {
    return StaticValues.MKT_CAP_FILE_PLATFORM;
  }
}
