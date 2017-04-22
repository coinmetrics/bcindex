package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.domain.Index;

import java.util.*;

/**
 * Created by rise on 4/8/17.
 */
public class IndexCalculator {

  private static final BcLog log = BcLog.getLogger(IndexCalculator.class);
  private final BusinessRules businessRules;
  private final double divisor;
  private final double constant;
  private final double divisorEven;
  private final double constantEven;

  private Map<String,Index> lastIndexList;
  private double lastIndexValue;
  private double lastSum;
  private Map<String,Index> lastIndexListEven;
  private double lastIndexValueEven;
  private double lastSumEven;

  public IndexCalculator() {
    businessRules = new BusinessRules();
    divisor = businessRules.getDivisor();
    constant = businessRules.getConstant();
    divisorEven = businessRules.getDivisorEven();
    constantEven = businessRules.getConstantEven();
  }

  public double getConstant() {
    return constant;
  }

  public double getConstantEven() {
    return constantEven;
  }

  public double getLastIndexValue() {
    return lastIndexValue;
  }

  public double getLastIndexValueEven() {
    return lastIndexValueEven;
  }

  public double updateLast(Map<String,Index> lastIndexes) {
    lastIndexList = lastIndexes;
    calculateMarketCap();
    lastIndexValue = calculateIndexValue();
    lastIndexValueEven = calculateIndexValueEven();
    return lastIndexValue;
  }

  private double getUsdPerBtc() {
    double numUsdPerBtc = -1;

    Index usdBtc = lastIndexList.get(Index.getBitCoinTicker());

    if (usdBtc != null) {
      numUsdPerBtc = usdBtc.getLast();
    }
    return numUsdPerBtc;
  }

  private IndexCalculator calculateMarketCap() {
    log.info("calculating market cap");

    lastIndexList.keySet().stream().forEach(ticker -> {

      Optional<Double> multiplier = businessRules.getMultipler(ticker);
      updateMarketCapIfValid(multiplier, ticker);
    });
    return this;
  }

  private double calculateIndexValue() {
    log.info("calculating index value");

    lastSum = 0;
    for (Index ticker : lastIndexList.values()) {
      if (ticker.isMktCapValid()) {
        lastSum += ticker.getMktCap();
      }
    }
    double btcResult = ((lastSum + getConstant())/divisor);
    double usdResult = btcResult*getUsdPerBtc();
    logCalculation(usdResult, btcResult);
    return usdResult;
  }

  private double calculateIndexValueEven() {
    log.info("calculating 10 even index value");

    lastSum = 0;
    for (Index ticker : lastIndexList.values()) {
      if (ticker.isMktCapValid()) {
        lastSum += ticker.getMktCap();
      }
    }
    double btcResultEven = ((lastSum + getConstantEven())/divisorEven);
    double usdResultEven = btcResultEven*getUsdPerBtc();
    logCalculationEven(usdResultEven, btcResultEven);
    return usdResultEven;
  }

  private void logCalculation(double UsdResult, double btcResult) {
    log.debug("last mkt cap sum: " + lastSum
        + " constant: " + getConstant() + " divsor: "
        + divisor + " usd-btc: " + getUsdPerBtc()
        + " index value USD=" + UsdResult + ", BTC=" + btcResult);
  }

  private void logCalculationEven(double UsdResultEven, double btcResultEven) {
    log.debug("last even sum: " + lastSum
        + " constant: " + getConstantEven() + " divsor: "
        + divisorEven + " usd-btc: " + getUsdPerBtc()
        + " index value USD=" + UsdResultEven + ", BTC=" + btcResultEven);
  }

  private void updateMarketCapIfValid(Optional<Double> multiplier, String ticker) {
    if (multiplier.isPresent()) {
      updateMarketCap(multiplier.get(), lastIndexList.get(ticker));
    }
    else {
      log.error("cannot calculate market cap for ticker '"
          + ticker + "' no multiplier exists in " + businessRules.getRulesFileName());
    }
  }

  private void updateMarketCap(double multiplier, Index indexForTicker) {
    double last = indexForTicker.getLast();
    double mktCap = last*multiplier;

    log.debug("updating ticker: " + indexForTicker.getName()
      + " last: " + last + " mkt cap: " + mktCap);

    indexForTicker.setMktCap(mktCap);
  }

  public void put(String name, double last, double mktCap) {
    Index idx = new Index().setName(name)
        .setLast(last)
        .setMktCap(mktCap);

    lastIndexList.put(idx.getName(), idx);
  }

  public Collection<Index> getSortedValues() {
    List<Index> sorted = new ArrayList<>(lastIndexList.values());
    Collections.reverse(sorted);
    return sorted;
  }

  public List<String> mktCapAsStrings() {
    List<String> idxDisplay = new ArrayList<>(lastIndexList.size());
    lastIndexList.values().stream().forEach(val -> {
      double mktCap = val.getMktCap();

      idxDisplay.add(val.getName()
          + " [mkt cap: " + mktCap
          + ", last price: " + val.getLast() + "]");
    });
    return idxDisplay;
  }
}
