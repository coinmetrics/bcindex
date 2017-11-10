package com.frobro.bcindex.web.model;

import com.frobro.bcindex.web.constants.StaticValues;
import com.frobro.bcindex.web.service.BusRulesForty;
import com.frobro.bcindex.web.service.BusinessRules;

public class BletchInForty extends BletchleyData {
  public static double getDivisor() {
    return StaticValues.DIVISOR_40;
  }

  public static double getDivisorEven() {
    return StaticValues.DIVISOR_EVEN_40;
  }

  @Override
  protected BusinessRules newBusinessRules() {
    return new BusRulesForty();
  }
}
