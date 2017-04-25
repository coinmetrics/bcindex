package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.domain.Index;
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
  private double lastIndexValue;
  private double lastIndexValueEven;

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

  public void updateLast(BletchleyData newData) {
    lastIndexList = newData.getLastIndexes();
    calculateMarketCap();
    lastIndexValue = calculateIndexValue();
    lastIndexValueEven = calculateIndexValueEven();
  }


  private double getUsdPerBtc() {
    double numUsdPerBtc = -1;

    Index usdBtc = lastIndexList.get(BletchleyData.getUsdBtcTicker());

    if (usdBtc != null) {
      numUsdPerBtc = usdBtc.getLast();
    }
    return numUsdPerBtc;
  }

  private IndexCalculator calculateMarketCap() {
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

  private double calculateIndexValue() {
    log.info("calculating index value");

    double lastSum = 0;
    for (Index ticker : lastIndexList.values()) {
      if (ticker.isMktCapValid()) {
        lastSum += ticker.getMktCap();
      }
    }
    double btcResult = ((lastSum + getConstant())/divisor);
    double usdResult = btcResult*getUsdPerBtc();
    logCalculation(lastSum, usdResult, btcResult);
    return usdResult;
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
    double usdResultEven = btcResultEven*getUsdPerBtc();
    logCalculationEven(lastSum, usdResultEven, btcResultEven);
    return usdResultEven;
  }

  private void logCalculation(double lastSum, double UsdResult, double btcResult) {
    log.debug("last mkt cap sum: " + lastSum
        + " constant: " + getConstant() + " divsor: "
        + divisor + " usd-btc: " + getUsdPerBtc()
        + " index value USD=" + UsdResult + ", BTC=" + btcResult);
  }

  private void logCalculationEven(double lastSum, double UsdResultEven, double btcResultEven) {
    log.debug("last even sum: " + lastSum
        + " even constant: " + getConstantEven() + " even divsor: "
        + divisorEven + " usd-btc: " + getUsdPerBtc()
        + " even index value USD=" + UsdResultEven + ", even BTC=" + btcResultEven);
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
