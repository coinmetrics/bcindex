package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.model.Ticker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by rise on 3/23/17.
 */
abstract class BusinessRules {
  private static final BcLog log = BcLog.getLogger(BusinessRules.class);
  private static final String DELIMINATOR = ",";
  private static final int NAME_POS = 0;
  private static final int MULT_POS = 1;
  private static final int EVEN_MULT_POS = 2;


  protected boolean shouldPopulate(Map<String, Ticker> map) {
    return map == null;
  }

  protected void populateValuesFromFile(Map<String,Ticker> map, String fileName) {
    InputStream data = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    List<String> lines = readLines(data);
    lines.stream().forEach(line -> {
      String[] vals = line.split(DELIMINATOR);

      String name = vals[NAME_POS];

      Ticker ticker = new Ticker(name);

      String multStr = vals[MULT_POS];
      Double multiplier = Double.parseDouble(multStr);
      ticker.setMultiplier(multiplier);

      String evenMultStr = vals[EVEN_MULT_POS];
      Double evenMult = Double.parseDouble(evenMultStr);
      ticker.setEvenMultiplier(evenMult);

      map.put(ticker.getName(), ticker);
    });
  }

  protected void logValues(Map<String,Ticker> map) {
    map.entrySet().stream().forEach(entry -> {
      log.debug("mapping ticker: " + entry.getKey()
          + " with multiplier: " + entry.getValue().getMultiplier()
          + " with even mult:  " + entry.getValue().getEvenMultiplier());
    });
  }

  private List<String> readLines(InputStream data) {
    return new BufferedReader(new InputStreamReader(data,
        StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
  }

  protected Optional<Double> extractMultiplier(Ticker ticker) {
    Double val = ticker.getMultiplier();
    return wrapInOptional(val);
  }

  protected Optional<Double> extractMultiplierEven(Ticker ticker) {
    return wrapInOptional(ticker.getEvenMultiplier());
  }

  protected Ticker getTicker(String tickerName, Map<String,Ticker> map) {
    Ticker ticker = map.get(tickerName);

    if (ticker == null) {
      logError(tickerName);
      // refactor this to not depend on the assumption
      // there will be a null check in the calling
      // method
      ticker = new Ticker(tickerName);
    }

    return ticker;
  }

  private void logError(String tickerName) {
    log.error("Error cannot find ticker: " + tickerName);
  }

  private Optional<Double> wrapInOptional(Double val) {
    return val == null ? Optional.empty() : Optional.of(val);
  }

  abstract public Optional<Double> getMultipler(String ticker);
  abstract public Optional<Double> getMultiplierEven(String ticker);

  abstract public double getDivisor();
  abstract public double getDivisorEven();
}

