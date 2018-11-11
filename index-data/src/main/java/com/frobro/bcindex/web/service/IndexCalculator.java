package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.model.BletchleyData;
import com.frobro.bcindex.web.model.Weight;
import com.frobro.bcindex.web.service.persistence.IndexDbDto;
import com.frobro.bcindex.web.service.rules.BusinessRules;

import java.util.*;

/**
 * Created by rise on 4/8/17.
 */
public abstract class IndexCalculator {

  private static final BcLog log = BcLog.getLogger(IndexCalculator.class);

  // for 10 idx
  protected double constantEven;
  protected double constant;

  // fields that live for the life of this instance
  protected final double divisor;
  protected final double divisorEven;
  protected final BusinessRules businessRules;
  // fields that are reset with every update
  private double lastUsdPerBtc;
  private Map<String,Double> weights;
  private Map<String,Double> weightsEven;
  protected long lastTimeStamp;
  protected Map<String,Index> lastIndexList;

  protected IndexCalculator(BusinessRules rules) {
    this.businessRules = rules;
    divisor = businessRules.getDivisor();
    divisorEven = businessRules.getDivisorEven();
    weights = new HashMap<>();
    weightsEven = new HashMap<>();
  }

  protected String indexName() {
    return businessRules.indexName();
  }

  abstract protected double buildFromSum(double lastSum,
                                         double constant,
                                         double divisor);

  abstract protected IndexDbDto newDbDto(double priceBtc,
                                         double lastUsdPerBtc,
                                         long time);
  
  public void updateLast(BletchleyData newData) {
    lastIndexList = newData.getLastIndexes();
    lastTimeStamp = newData.getTimeStamp();
    lastUsdPerBtc = newData.getPriceUsdBtc();

    resetMktWeights();
    businessRules.calculateMarketCap(newData);
  }

  private void resetMktWeights() {
    weights = new HashMap<>();
    weightsEven = new HashMap<>();
  }

  public IndexDbDto calcuateOddIndex() {
    return calculateOddIndex(lastUsdPerBtc, lastTimeStamp);
  }

  public IndexDbDto calculateEvenIndex() {
    return calculateEvenIndex(lastUsdPerBtc, lastTimeStamp);
  }

  private IndexDbDto calculateOddIndex(double usdPerBtc, long timeStamp) {
    double sum = calculateSum();
    double indexPrice = calculateIndexValueBtc(sum);

    IndexDbDto dto = newDbDto(indexPrice, usdPerBtc, timeStamp);
    logUsdCalc("original", dto.indexValueBtc, usdPerBtc,
        dto.indexValueUsd, dto.timeStamp);

    weights = Weight.calculateWeight(lastIndexList, sum);
    return dto;
  }

  private IndexDbDto calculateEvenIndex(double usdPerBtc, long timeStamp) {
    double sum = calculateSumEven();
    double indexPrice = calculateIndexValueEven(sum);

    IndexDbDto dto = newDbDto(indexPrice, usdPerBtc, timeStamp);
    logUsdCalc("even",dto.indexValueBtc, usdPerBtc,
        dto.indexValueUsd, timeStamp);

    weightsEven = Weight.calculateWeight(lastIndexList, sum);

    return dto;
  }

  private void logUsdCalc(String idxName, double btcValue,
                          double usdPerBtc, double result, long time) {
    log.debug(indexName() + ": '" + idxName + "' at time: '" + new Date(time)
        + "' in BTC=" + btcValue + " times "
        + "last btc price=" + usdPerBtc + ". Which makes the index in USD="
        + result + "\n");
  }

  private double calculateSum() {
    double lastSum = 0;
    for (Index ticker : lastIndexList.values()) {
      if (ticker.isMktCapValid()) {
        lastSum += ticker.getMultiplier();
      }
      else {
        log.error("invalid market cap for " + ticker.getName());
      }
    }
    return lastSum;
  }

  private double calculateIndexValueBtc(double lastSum) {
    log.debug("calculating index value for " + indexName());

    double btcValue = buildFromSum(lastSum,constant,divisor);
    logCalculation(lastSum, btcValue);

    return btcValue;
  }

  private double calculateSumEven() {
    double lastSum = 0;
    for (Index ticker : lastIndexList.values()) {
      if (ticker.isMktCapValid()) {
        lastSum += ticker.getEvenMult();
      }
    }
    return lastSum;
  }

  private double calculateIndexValueEven(double lastSum) {
    log.debug("calculating even index for " + indexName());

    double btcResultEven = buildFromSum(lastSum,constantEven,divisorEven);
    logCalculationEven(lastSum, btcResultEven);
    return btcResultEven;
  }

  private void logCalculation(double lastSum, double btcResult) {
    log.debug("last mkt cap sum: " + lastSum
        + " constant: " + constant + " divsor: "
        + divisor + " usd-btc: " + lastUsdPerBtc
        + ", BTC=" + btcResult);
  }

  private void logCalculationEven(double lastSum, double btcResultEven) {
    log.debug("last even sum: " + lastSum
        + " even constant: " + constantEven + " even divsor: "
        + divisorEven + " usd-btc: " + lastUsdPerBtc
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

  public Map<String,Double> getWeights() {
    return new HashMap<>(weights);
  }

  public Map<String,Double> getWeightsEven() {
    return new HashMap<>(weightsEven);
  }
}
