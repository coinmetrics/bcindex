package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by rise on 3/23/17.
 */
public class BusinessRules {
  private static final BcLog log = BcLog.getLogger(BusinessRules.class);
  private static final double DIVISOR = 21599771.0134701;
  private static final double DIVISOR_EVEN = 14794508.5006863;
  private static final double CONSTANT = 16280750;
  private static final double CONSTANT_EVEN = 2285053.56118387;
  private static final String MKT_CAP_FILE = "business_rules/mkt_cap.txt";
  private static final String DELIMINATOR = "=";
  private static Map<String,Double> multiplerMapping;

  public BusinessRules() {
    if (multiplerMapping == null) {
      multiplerMapping = populateMultipliers();
    }
  }

  private Map<String,Double> populateMultipliers() {
    Properties prop = new Properties();
    InputStream input = null;
    Map<String,Double> MULT = new HashMap<>();
    try {

      input = Thread.currentThread().getContextClassLoader().getResourceAsStream(MKT_CAP_FILE);
      prop.load(input);

      prop.entrySet().stream().forEach(entry -> {
        String[] vals = entry.toString().split(DELIMINATOR);

        if (vals.length != 2) {
          throw new RuntimeException("could not parse the property: " + entry);
        }

        String ticker = String.valueOf(entry.getKey());
        Double multiplier = Double.parseDouble(String.valueOf(entry.getValue()));
        log.debug("mapping ticker: " + ticker + " with multiplier: " + multiplier);
        MULT.put(ticker,multiplier);
      });

    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    return MULT;
  }

  public double getConstant() {
    return CONSTANT;
  }

  public double getConstantEven() {
    return CONSTANT_EVEN;
  }

  public double getDivisor() {
    return DIVISOR;
  }

  public double getDivisorEven() {
    return DIVISOR_EVEN;
  }

  public Set<String> getIndexes() {
    return multiplerMapping.keySet();
  }

  public Optional<Double> getMultipler(String ticker) {
    Double val = multiplerMapping.get(ticker);

    if (val == null) {
      System.out.println("VAL IS NULL! NULL NULL");
      System.out.println("size: " + multiplerMapping.size());
      multiplerMapping.entrySet().stream().forEach(v -> {
        System.out.println(v);
      });
    }

    Optional optional = val == null ? Optional.empty() : Optional.of(val);

    return optional;
  }

  public String getRulesFileName() {
    return MKT_CAP_FILE;
  }
}
