package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.service.persistence.IndexDbDto;
import com.frobro.bcindex.web.service.rules.BusRulesTen;
import com.frobro.bcindex.web.service.rules.BusinessRules;

/**
 * Created by rise on 6/11/17.
 */
public class IndexCalculatorTen extends IndexCalculator {

  public IndexCalculatorTen() {
    super(new BusRulesTen());
    this.constant = BusRulesTen.getConstant();
    this.constantEven = BusRulesTen.getConstantEven();
  }

  @Override
  protected double buildFromSum(double lastSum, double constant, double divisor) {
    return (lastSum + constant)/divisor;
  }

  @Override
  protected IndexDbDto newDbDto(double indexPrice, double usdPerBtc, long time) {
    IndexDbDto dto = new IndexDbDto();
    dto.indexValueBtc = toUsd(indexPrice, usdPerBtc);
    dto.indexValueUsd = indexPrice; 
    dto.timeStamp = time;
    return dto;
  }

  private double toUsd(double indexPrice, double usdPerBtc) {
    return indexPrice/usdPerBtc;
  }
}
