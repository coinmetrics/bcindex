package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.service.persistence.IndexDbDto;
import com.frobro.bcindex.web.service.rules.BusinessRules;

import java.util.Optional;

public class IndexCalculatorSector extends IndexCalculator {
  private static final BcLog log = BcLog.getLogger(IndexCalculatorSector.class);

  public IndexCalculatorSector(BusinessRules businessRules) {
    super(businessRules);
  }

  public IndexDbDto calculateEvenIndex() {
    throw new UnsupportedOperationException("Sector indexes do not support" +
        " even index calculation");
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
