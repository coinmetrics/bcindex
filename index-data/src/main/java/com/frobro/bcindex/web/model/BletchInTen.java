package com.frobro.bcindex.web.model;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.constants.StaticValues;
import com.frobro.bcindex.web.service.rules.BusRulesTen;
import com.frobro.bcindex.web.service.rules.BusinessRules;

/**
 * Created by rise on 7/13/17.
 */
public class BletchInTen extends BletchleyData {
  private static final BcLog log = BcLog.getLogger(BletchInTen.class);

  public static double getConstant() {
    return StaticValues.CONSTANT_TEN;
  }

  public static double getConstantEven() {
    return StaticValues.CONSTANT_EVEN_TEN;
  }

  public static double getDivisor() {
    return StaticValues.DIVISOR_TEN;
  }

  public static double getDivisorEven() {
    return StaticValues.DIVISOR_EVEN_TEN;
  }

  @Override
  protected BusinessRules newBusinessRules() {
    return new BusRulesTen();
  }
}
