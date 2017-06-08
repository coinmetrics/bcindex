package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.model.BletchleyData;
import com.frobro.bcindex.web.model.Ticker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by rise on 3/23/17.
 */
public class BusinessRules {
  private static final BcLog log = BcLog.getLogger(BusinessRules.class);
  private static final double DIVISOR = 21901918.35;
  private static final double DIVISOR_EVEN = 10964215.29;
  private static final double CONSTANT = 16333862;
  private static final double CONSTANT_EVEN = 3022990.404;
  private static final double DIVISOR_20 = 8269507.429;
  private static final double DIVISOR_EVEN_20 = 6990359.243;
  private static final String MKT_CAP_FILE = "business_rules/May_rebal.csv";
  private static final String MKT_CAP_FILE_20 = "business_rules/20_may_rebal.csv";
  private static final String DELIMINATOR = ",";
  private static final int NAME_POS = 0;
  private static final int MULT_POS = 1;
  private static final int EVEN_MULT_POS = 2;
  private static Map<String,Ticker> tickers;

  public BusinessRules() {
    if (tickers == null) {
      populateMultipliers();
      logMultiplers();
    }
  }

  private void populateMultipliers() {
    tickers = new HashMap<>();

      InputStream data = Thread.currentThread().getContextClassLoader().getResourceAsStream(MKT_CAP_FILE);
      List<String> lines = readLines(data);
      lines.stream().forEach(line -> {
        String[] vals = line.split(DELIMINATOR);

        String name = vals[NAME_POS];

        Ticker ticker = new Ticker(name);

        String multStr = vals[MULT_POS];
        Double multiplier = Double.parseDouble(multStr);
        ticker.setMarketCap(multiplier);

        String evenMultStr = vals[EVEN_MULT_POS];
        Double evenMult = Double.parseDouble(evenMultStr);
        ticker.setEvenMultiplier(evenMult);

        tickers.put(ticker.getName(), ticker);
      });

    Ticker btcTicker = getBtcTicker();
    tickers.put(btcTicker.getName(), btcTicker);
  }

  private void logMultiplers() {
    tickers.entrySet().stream().forEach(entry -> {
      log.debug("mapping ticker: " + entry.getKey()
          + " with multiplier: " + entry.getValue().getMarketCap()
          + " with even mult:  " + entry.getValue().getEvenMultiplier());
    });
  }

  private List<String> readLines(InputStream data) {
    return new BufferedReader(new InputStreamReader(data,
        StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
  }

  private Ticker getBtcTicker() {
    Ticker btcTicker = new Ticker(BletchleyData.getUsdBtcTicker());
    btcTicker.setEvenMultiplier(0).setMarketCap(0);

    return btcTicker;
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

  public double getDivisor20() {
    return DIVISOR_20;
  }

  public double getDivisorEven20() {
    return DIVISOR_EVEN_20;
  }

  public Set<String> getIndexes() {
    return new HashSet<>(tickers.keySet());
  }

  public Optional<Double> getEvenMultipler(String tickerName) {
    Ticker ticker = getTicker(tickerName);
    Double val = ticker.getEvenMultiplier();
    return wrapInOptional(val);
  }

  public Optional<Double> getMultipler(String tickerName) {
    Ticker ticker = getTicker(tickerName);
    Double val = ticker.getMarketCap();
    return wrapInOptional(val);
  }

  private Ticker getTicker(String tickerName) {
    Ticker ticker = tickers.get(tickerName);

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
    System.out.println("Error cannot find ticker: " + tickerName);
    System.out.println("size: " + tickers.size());
    tickers.entrySet().stream().forEach(v -> {
      System.out.println(v);
    });
  }

  private Optional<Double> wrapInOptional(Double val) {
    return val == null ? Optional.empty() : Optional.of(val);
  }

  public String getRulesFileName() {
    return MKT_CAP_FILE;
  }
}
