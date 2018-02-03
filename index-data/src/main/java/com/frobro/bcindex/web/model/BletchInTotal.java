package com.frobro.bcindex.web.model;

import com.frobro.bcindex.web.constants.StaticValues;
import com.frobro.bcindex.web.service.rules.BusRulesTotal;
import com.frobro.bcindex.web.service.rules.BusinessRules;

public class BletchInTotal extends BletchleyData {
  public static double getDivisor() {
    return StaticValues.DIVISOR_TOTAL;
  }

  public static double getDivisorEven() {
    return StaticValues.DIVISOR_EVEN_TOTAL;
  }

  @Override
  protected BusinessRules newBusinessRules() {
    return new BusRulesTotal();
  }
}
