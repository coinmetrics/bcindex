package com.frobro.bcindex.web.service;

import java.text.DecimalFormat;

/**
 * Created by rise on 6/5/17.
 */
public class DoubleFormatter {
  private final static DecimalFormat FORMATTER = new DecimalFormat("#.##");

  public static double format(double raw) {
    String formatted = FORMATTER.format(raw);
    return Double.parseDouble(formatted);
  }

  private DoubleFormatter() {}
}
