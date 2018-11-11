package com.frobro.bcindex.web.service.rules;

import com.frobro.bcindex.core.service.BletchFiles;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.model.BletchleyData;
import com.frobro.bcindex.web.model.Ticker;
import com.frobro.bcindex.web.service.MultiplierService;

import java.util.*;

/**
 * Created by rise on 3/23/17.
 */
abstract public class BusinessRules {
  private static final BcLog LOG = BcLog.getLogger(BusinessRules.class);
  private static final int EVEN_MULT_POS = 2;
  protected static final String DELIMINATOR = ",";
  protected static final int NAME_POS = 0;
  protected static final int MULT_POS = 1;

  protected boolean shouldPopulate(Map<String, Ticker> map) {
    return map == null;
  }

  protected void populateValuesFromFile(Map<String,Ticker> map, String fileName) {
    List<String> lines = BletchFiles.linesToList(fileName);
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

  protected void logValues(Map<String,Ticker> map, String index) {
    LOG.debug(index + " multipliers");
    map.entrySet().stream().forEach(entry -> {
      LOG.debug("mapping ticker: " + entry.getKey()
          + " with multiplier: " + entry.getValue().getMultiplier()
          + " with even mult:  " + entry.getValue().getEvenMultiplier());
    });
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
    LOG.error("Error cannot find ticker: " + tickerName);
  }

  private Optional<Double> wrapInOptional(Double val) {
    return val == null ? Optional.empty() : Optional.of(val);
  }

  public Optional<Double> getMultipler(String tickerName) {
    return extractMultiplier(getTicker(tickerName, getTickers()));
  }

  public Optional<Double> getMultiplierEven(String tickerName) {
    return extractMultiplierEven(getTicker(tickerName, getTickers()));
  }

  public Set<String> getIndexes() {
    return new HashSet<>(getTickers().keySet());
  }

  public List<Ticker> getTickerList() {
    return new ArrayList<>(getTickers().values());
  }

  public boolean hasEven() {
    return Boolean.TRUE;
  }

  public BletchleyData calculateMarketCap(BletchleyData data) {
    LOG.debug("calculating market cap for " + indexName());

    Map<String,Index> indexList = data.getLastIndexes();
    MultiplierService multService = new MultiplierService(indexList);

    indexList.keySet().stream().forEach(ticker -> {
      Optional<Double> multiplier = getMultipler(ticker);
      multService.updateMarketCapIfValid(multiplier, ticker);

    });
    return data;
  }

  abstract protected Map<String,Ticker> getTickers();
  abstract public double getDivisor();
  abstract public double getDivisorEven();
  abstract public String indexName();
}

