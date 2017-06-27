package com.frobro.bcindex.web.service;

import java.text.DecimalFormat;

/**
 * Created by rise on 6/5/17.
 */
public class DoubleFormatter {
  private final static DecimalFormat USD_FORMAT = new DecimalFormat("#.00");
  private final static DecimalFormat BTC_FORMAT = new DecimalFormat("#.000");

  public static double formatUsd(double raw) {
    return format(raw, USD_FORMAT);
  }

  public static double formatBtc(double raw) {
    return format(raw, BTC_FORMAT);
  }

  private static double format(double raw, DecimalFormat formatter) {
    String formatted = formatter.format(raw);
    return Double.parseDouble(formatted);
  }

  private DoubleFormatter() {}
}
