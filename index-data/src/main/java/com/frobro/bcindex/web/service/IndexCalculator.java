package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.domain.JpaEvenIndex;
import com.frobro.bcindex.web.domain.JpaIndex;
import com.frobro.bcindex.web.model.BletchleyData;
import com.frobro.bcindex.web.service.persistence.IndexDbDto;

import java.util.*;

/**
 * Created by rise on 4/8/17.
 */
public abstract class IndexCalculator {

  private static final BcLog log = BcLog.getLogger(IndexCalculator.class);
  // for 10 idx
  private final BusinessRules businessRules;
  protected double constantEven;
  protected double constant;
  private double lastUsdPerBtc;

  protected double divisor;
  protected double divisorEven;
  protected long lastTimeStamp;
  protected Map<String,Index> lastIndexList;

  public IndexCalculator() {
    businessRules = newBusinessRules();
    divisor = businessRules.getDivisor();
    divisorEven = businessRules.getDivisorEven();
  }

  abstract protected BusinessRules newBusinessRules();
  abstract protected String indexName();
  abstract protected double buildFromSum(double lastSum,
                                         double constant,
                                         double divisor);

  public double getConstant() {
    return constant;
  }

  public double getConstantEven() {
    return constantEven;
  }

  public void updateLast(BletchleyData newData) {
    lastIndexList = newData.getLastIndexes();
    lastTimeStamp = newData.getTimeStamp();

    calculateMarketCap();

    lastUsdPerBtc = getUsdPerBtc();
  }

  public IndexDbDto calcuateOddIndex() {
    return calculateOddIndex(lastUsdPerBtc, lastTimeStamp);
  }

  public IndexDbDto calculateEvenIndex() {
    return calculateEvenIndex(lastUsdPerBtc, lastTimeStamp);
  }

  private IndexDbDto calculateOddIndex(double usdPerBtc, long timeStamp) {
    double btcValue = calculateIndexValueBtc();
    double usdValue = btcValue*usdPerBtc;
    logUsdCalc("original", btcValue, usdPerBtc, usdValue, timeStamp);

    return newDbDto(btcValue, usdValue, timeStamp);
  }

  private IndexDbDto newDbDto(double priceBtc, double priceUsd, long time) {
    IndexDbDto dto = new IndexDbDto();
    dto.indexValueBtc = priceBtc;
    dto.indexValueUsd = priceUsd;
    dto.timeStamp = time;
    return dto;
  }

  private IndexDbDto calculateEvenIndex(double usdPerBtc, long timeStamp) {
    double btcEvenValue = calculateIndexValueEven();
    double usdEvenValue = btcEvenValue*usdPerBtc;
    logUsdCalc("even",btcEvenValue, usdPerBtc, usdEvenValue, timeStamp);

//    JpaEvenIndex lastEvenIndex = JpaEvenIndex.create();
//    lastEvenIndex.setIndexValueBtc(btcEvenValue)
//                      .setTimeStamp(timeStamp)
//                      .setIndexValueUsd(usdEvenValue);
    return newDbDto(btcEvenValue, usdEvenValue, timeStamp);
  }

  private void logUsdCalc(String idxName, double btcValue,
                          double usdPerBtc, double result, long time) {
    log.debug(indexName() + ": '" + idxName + "' at time: '" + new Date(time)
        + "' in BTC=" + btcValue + " times "
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
    log.debug("calculating market cap");

    lastIndexList.keySet().stream().forEach(ticker -> {

      Optional<Double> multiplier = businessRules.getMultipler(ticker);
      MultiplierService multService = new MultiplierService(lastIndexList)
          .updateMarketCapIfValid(multiplier, ticker);

      Optional<Double> evenMultipler = businessRules.getMultiplierEven(ticker);
      multService.updateEvenMktCapIfValid(evenMultipler, ticker);
    });
    return this;
  }

  private double calculateIndexValueBtc() {
    log.debug("calculating index value");

    double lastSum = 0;
    for (Index ticker : lastIndexList.values()) {
      if (ticker.isMktCapValid()) {
        lastSum += ticker.getMktCap();
      }
    }

    double btcValue = buildFromSum(lastSum,constant,divisor);
    logCalculation(lastSum, btcValue);

    return btcValue;
  }

  private double calculateIndexValueEven() {
    log.debug("calculating 10 even index value");

    double lastSum = 0;
    for (Index ticker : lastIndexList.values()) {
      if (ticker.isMktCapValid()) {
        lastSum += ticker.getEvenMult();
      }
    }
    double btcResultEven = buildFromSum(lastSum,constantEven,divisorEven);
    logCalculationEven(lastSum, btcResultEven);
    return btcResultEven;
  }

  private void logCalculation(double lastSum, double btcResult) {
    log.debug("last mkt cap sum: " + lastSum
        + " constant: " + constant + " divsor: "
        + divisor + " usd-btc: " + getUsdPerBtc()
        + ", BTC=" + btcResult);
  }

  private void logCalculationEven(double lastSum, double btcResultEven) {
    log.debug("last even sum: " + lastSum
        + " even constant: " + constantEven + " even divsor: "
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
}
