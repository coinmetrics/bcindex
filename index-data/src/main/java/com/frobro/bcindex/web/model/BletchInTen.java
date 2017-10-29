package com.frobro.bcindex.web.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.constants.StaticValues;
import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.service.BusRulesTen;
import com.frobro.bcindex.web.service.BusinessRules;
import com.frobro.bcindex.web.service.CurrPairJson;

import java.io.IOException;
import java.util.*;

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
