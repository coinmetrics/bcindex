package com.frobro.bcindex.core.db.service;

import java.text.DecimalFormat;

public class DoubleService {
  private final static DecimalFormat USD_FORMAT = new DecimalFormat("#.00");
  private final static DecimalFormat BTC_FORMAT = new DecimalFormat("0.00###");
  private final static DecimalFormat WEIGHT_FORMAT = new DecimalFormat("0.00###");

  public static double formatUsd(double raw) {
    return format(raw, USD_FORMAT);
  }

  public static double formatBtc(double raw) {
    return format(raw, BTC_FORMAT);
  }

  public static double formatWeight(double raw) {
    return format(raw, WEIGHT_FORMAT);
  }

  private static double format(double raw, DecimalFormat formatter) {
    String formatted = formatter.format(raw);
    return Double.parseDouble(formatted);
  }

  private DoubleService() {}
}
