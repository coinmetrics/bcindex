package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.domain.JpaEvenIndex;
import com.frobro.bcindex.web.domain.JpaIndex;
import com.frobro.bcindex.web.model.BletchleyData;

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
  private JpaIndex lastIndex;
  private JpaEvenIndex lastEvenIndex;

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

  public JpaIndex getLastIndex() {
    return lastIndex;
  }

  public JpaEvenIndex getLastIndexEven() {
    return lastEvenIndex;
  }

  public double getConstantEven() {
    return constantEven;
  }

  public double getLastIndexValue() {
    if (lastIndex == null) {
      throw new IllegalStateException("the index value has not "
        + "been calculated yet.");
    }
    return lastIndex.getIndexValueUsd();
  }

  public double getLastIndexValueEven() {
    return lastEvenIndex.getIndexValueUsd();
  }

  public void updateLast(BletchleyData newData) {
    lastIndexList = newData.getLastIndexes();
    long timeStamp = newData.getTimeStamp();

    calculateMarketCap();

    double usdPerBtc = getUsdPerBtc();

    calculateOddIndex(usdPerBtc, timeStamp);
    calculateEvenIndex(usdPerBtc, timeStamp);
  }

  private void calculateOddIndex(double usdPerBtc, long timeStamp) {
    double btcValue = calculateIndexValueBtc();
    double usdResult = btcValue*usdPerBtc;
    logUsdCalc("original", btcValue, usdPerBtc, usdResult, timeStamp);

    this.lastIndex = JpaIndex.create()
        .setTimeStamp(timeStamp)
        .setIndexValueBtc(btcValue)
        .setIndexValueUsd(usdResult);
  }

  private void calculateEvenIndex(double usdPerBtc, long timeStamp) {
    double btcEvenValue = calculateIndexValueEven();
    double usdEvenValue = btcEvenValue*usdPerBtc;
    logUsdCalc("even",btcEvenValue, usdPerBtc, usdEvenValue, timeStamp);

    this.lastEvenIndex = JpaEvenIndex.create();
    this.lastEvenIndex.setIndexValueBtc(btcEvenValue)
                      .setTimeStamp(timeStamp)
                      .setIndexValueUsd(usdEvenValue);
  }


  private void logUsdCalc(String idxName, double btcValue,
                          double usdPerBtc, double result, long time) {
    log.debug("INDEX: '" + idxName + "' at time: '" + new Date(time) + "' in BTC=" + btcValue + " times "
        + "last btc price=" + usdPerBtc + ". Which makes the index in USD=" + result);
  }

  private double getUsdPerBtc() {
    double numUsdPerBtc = -1;

    Index usdBtc = lastIndexList.get(BletchleyData.getUsdBtcTicker());

    if (usdBtc != null) {
      numUsdPerBtc = usdBtc.getLast();
    }
    return numUsdPerBtc;
  }

  protected IndexCalculator calculateMarketCap() {
    log.info("calculating market cap");

    lastIndexList.keySet().stream().forEach(ticker -> {

      Optional<Double> multiplier = businessRules.getMultipler(ticker);
      MultiplierService multService = new MultiplierService(lastIndexList)
          .updateMarketCapIfValid(multiplier, ticker);

      Optional<Double> evenMultipler = businessRules.getEvenMultipler(ticker);
      multService.updateEvenMktCapIfValid(evenMultipler, ticker);
    });
    return this;
  }

  private double calculateIndexValueBtc() {
    log.info("calculating index value");

    double lastSum = 0;
    for (Index ticker : lastIndexList.values()) {
      if (ticker.isMktCapValid()) {
        lastSum += ticker.getMktCap();
      }
    }

    double btcValue = ((lastSum + getConstant())/divisor);
    logCalculation(lastSum, btcValue);

    return btcValue;
  }

  private double calculateIndexValueEven() {
    log.info("calculating 10 even index value");

    double lastSum = 0;
    for (Index ticker : lastIndexList.values()) {
      if (ticker.isMktCapValid()) {
        lastSum += ticker.getEvenMult();
      }
    }
    double btcResultEven = ((lastSum + getConstantEven())/divisorEven);
    logCalculationEven(lastSum, btcResultEven);
    return btcResultEven;
  }

  private void logCalculation(double lastSum, double btcResult) {
    log.debug("last mkt cap sum: " + lastSum
        + " constant: " + getConstant() + " divsor: "
        + divisor + " usd-btc: " + getUsdPerBtc()
        + ", BTC=" + btcResult);
  }

  private void logCalculationEven(double lastSum, double btcResultEven) {
    log.debug("last even sum: " + lastSum
        + " even constant: " + getConstantEven() + " even divsor: "
        + divisorEven + " usd-btc: " + getUsdPerBtc()
        + ", even BTC=" + btcResultEven);
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
