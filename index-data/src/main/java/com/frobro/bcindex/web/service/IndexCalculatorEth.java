package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.constants.StaticValues;
import com.frobro.bcindex.web.model.BletchInTwenty;
import com.frobro.bcindex.web.service.persistence.IndexDbDto;

/**
 * Created by rise on 7/16/17.
 */
public class IndexCalculatorEth extends IndexCalculator {
  private static final BcLog log = BcLog.getLogger(IndexCalculatorEth.class);

  @Override
  protected double buildFromSum(double lastSum, double constant, double divisor) {
    return lastSum/divisor;
  }

  @Override
  protected BusinessRules newBusRules() {
    return new BusRulesEth();
  }

  @Override
  protected String indexName() {
    return "ETHEREUM INDEX";
  }

  @Override
  protected double getDivisor() {
    return StaticValues.DIVISOR_ETHER;
  }

  @Override
  protected double getDivisorEven() {
    return StaticValues.DIVISOR_EVEN_ETHER;
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
