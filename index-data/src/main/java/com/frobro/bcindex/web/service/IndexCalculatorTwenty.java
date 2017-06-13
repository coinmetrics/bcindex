package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.service.persistence.IndexDbDto;

/**
 * Created by rise on 6/11/17.
 */
public class IndexCalculatorTwenty extends IndexCalculator {
  private static final BcLog log = BcLog.getLogger(IndexCalculatorTwenty.class);

  @Override
  protected BusinessRules newBusinessRules() {
    return new BusRulesTwenty();
  }

  @Override
  protected double buildFromSum(double lastSum, double constant, double divisor) {
    return lastSum/divisor;
  }

  @Override
  protected String indexName() {
    return "20 INDEX";
  }

  @Override
  protected IndexDbDto newDbDto(double indexPrice, double usdPerBtc, long time) {
    IndexDbDto dto = new IndexDbDto();
    dto.indexValueBtc = toBtc(indexPrice,usdPerBtc);
    dto.indexValueUsd = indexPrice;
    dto.timeStamp = time;
    return dto;
  }

  private double toBtc(double px, double usdBtc) {
    return px/usdBtc;
  }
}
