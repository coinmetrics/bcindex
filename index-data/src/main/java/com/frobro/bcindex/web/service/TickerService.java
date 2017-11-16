package com.frobro.bcindex.web.service;

import java.io.IOException;
import java.util.*;

import com.frobro.bcindex.core.db.domain.*;
import com.frobro.bcindex.core.db.service.*;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.model.BletchInEth;
import com.frobro.bcindex.web.model.BletchInForty;
import com.frobro.bcindex.web.model.BletchInTen;
import com.frobro.bcindex.web.model.BletchInTwenty;
import com.frobro.bcindex.web.service.apis.CryptoCompare;
import com.frobro.bcindex.web.service.persistence.IndexDbDto;

/**
 * Created by rise on 3/23/17.
 */
public class TickerService {

  private static final BcLog log = BcLog.getLogger(TickerService.class);
  private final CryptoCompare cryptoCompare = new CryptoCompare();
  // calculators
  private IndexCalculator indexCalculatorTen = new IndexCalculatorTen();
  private IndexCalculator indexCalculatorTwenty = new IndexCalculatorTwenty();
  private IndexCalculator indexCalculatorEth = new IndexCalculatorEth();
  private IndexCalculator indexCalculatorForty = new IndexCalculatorForty();
  // repos
  PrimeRepo repo;
  // db data
  private IndexDbDto lastIndex;
  private IndexDbDto lastEvenIndex;
  private IndexDbDto lastTwentyIdx;
  private IndexDbDto lastEvenTwentyIdx;
  private IndexDbDto lastIdxEth;
  private IndexDbDto lastIdxEthEven;
  private IndexDbDto lastIdxForty;
  private IndexDbDto lastIdxFortyEven;

  // input data
  private BletchInTen inputDataTen = new BletchInTen();
  private BletchInTwenty inputDataTwenty = new BletchInTwenty();
  private BletchInEth inputEth = new BletchInEth();
  private BletchInForty inputForty = new BletchInForty();

  public void setIndexRepo(IndexRepo repo, EvenIdxRepo eRepo,
                           TwentyRepo tRepo, TwentyEvenRepo teRepo,
                           EthRepo etRepo, EthEvenRepo eteRepo,
                           FortyIdxRepo fRepo, FortyEvenIdxRepo feRepo) {

    this.repo = PrimeRepo.getRepo(repo, eRepo,
                                  tRepo, teRepo,
                                  etRepo, eteRepo,
                                  fRepo, feRepo);
  }

  public TickerService updateTickers() {
    try {

      update();
      saveIndices();

    } catch (IOException ioe) {
      log.error("could not successfully update. ", ioe);
    }
    return this;
  }

  private void update() throws IOException {
    log.debug("updating latest data");

    clearInputData();

    // get bit coin latest
    updateTickerBtc();

    // batch all api calls
    cryptoCompare.batchCoins(inputDataTen.getMembers())
               .batchCoins(inputDataTwenty.getMembers())
               .batchCoins(inputEth.getMembers())
               .batchCoins(inputForty.getMembers());

    // call the api to get the data
    Map<String,Index> apiData = cryptoCompare.callBatchedData();

    long time = System.currentTimeMillis();

    // update 10 idx
    inputDataTen.filterAndSet(apiData);
    inputDataTen.setLastUpdate(time);
    calculateAndSetIndexesTen(inputDataTen);

    // update 20 idx
    inputDataTwenty.filterAndSet(apiData);
    inputDataTwenty.setLastUpdate(time);
    calculateAndSetIndexesTwenty(inputDataTwenty);

    // update ETH idx
    inputEth.filterAndSet(apiData);
    inputEth.setLastUpdate(time);
    calculateAndSetIndexesEth(inputEth);

    // update forty idx
    inputForty.filterAndSet(apiData);
    inputForty.setLastUpdate(time);
    calculateAndSetIndexesForty(inputForty);

    logIndexSummary();
  }

  private void logIndexSummary() {
    String summary = "\nten      [btc=" + lastIndex.indexValueBtc + ", usd=" + lastIndex.indexValueUsd + "]\n"
                     + "ten even [btc=" + lastEvenIndex.indexValueBtc + ", usd=" + lastEvenIndex.indexValueUsd + "]\n"
        + "-----------------------------\n"
        + "twenty      [btc=" + lastTwentyIdx.indexValueBtc + ", usd=" + lastTwentyIdx.indexValueUsd + "]\n"
        + "twenty even [btc=" + lastEvenTwentyIdx.indexValueBtc + ", usd=" + lastEvenTwentyIdx.indexValueUsd + "]\n"
        + "-----------------------------\n"
        + "forty      [btc=" + lastIdxForty.indexValueBtc + ", usd=" + lastIdxForty.indexValueUsd + "]\n"
        + "forty even [btc=" + lastIdxFortyEven.indexValueBtc + ", usd=" + lastIdxFortyEven.indexValueUsd + "]\n"
        + "-----------------------------\n"
        + "eth      [btc=" + lastIdxEth.indexValueBtc + ", usd=" + lastIdxEth.indexValueUsd + "]\n"
        + "eth even [btc=" + lastIdxEthEven.indexValueBtc + ", usd=" + lastIdxEthEven.indexValueUsd + "]\n";
        log.debug(summary);
  }

  private void clearInputData() {
    inputDataTen = new BletchInTen();
    inputDataTwenty = new BletchInTwenty();
  }

  private void calculateAndSetIndexesTwenty(BletchInTwenty data) {
    indexCalculatorTwenty.updateLast(data);
    lastTwentyIdx = indexCalculatorTwenty.calcuateOddIndex();
    lastEvenTwentyIdx = indexCalculatorTwenty.calculateEvenIndex();
  }

  private void calculateAndSetIndexesEth(BletchInEth data) {
    indexCalculatorEth.updateLast(data);
    lastIdxEth = indexCalculatorEth.calcuateOddIndex();
    lastIdxEthEven = indexCalculatorEth.calculateEvenIndex();
  }

  private void calculateAndSetIndexesForty(BletchInForty data) {
    indexCalculatorForty.updateLast(data);
    lastIdxForty = indexCalculatorForty.calcuateOddIndex();
    lastIdxFortyEven = indexCalculatorForty.calculateEvenIndex();
  }

  private void calculateAndSetIndexesTen(BletchInTen data) {
    indexCalculatorTen.updateLast(data);
    lastIndex = indexCalculatorTen.calcuateOddIndex();
    lastEvenIndex = indexCalculatorTen.calculateEvenIndex();
  }

  public void updateTickerBtc() throws IOException {
    // get the value
    double btcPrice = cryptoCompare.getBtcPrice();
    // set the value
    inputDataTen.setLastUsdBtc(btcPrice);
    inputDataTwenty.setLastUsdBtc(btcPrice);
    inputEth.setLastUsdBtc(btcPrice);
    inputForty.setLastUsdBtc(btcPrice);
  }

  public void saveIndices() {
    saveIndexTen();
    saveIndexTwenty();
    saveIndexEth();
    saveIndexForty();
  }

  private void saveIndexTen() {
    JpaIndexTen idx = new JpaIndexTen();
    populateJpa(idx, lastIndex);
    repo.saveTen(idx);

    JpaEvenIndex eIdx = new JpaEvenIndex();
    populateJpa(eIdx, lastEvenIndex);
    repo.saveTenEven(eIdx);
  }

  private void saveIndexTwenty() {
    JpaIdxTwenty idx = new JpaIdxTwenty();
    populateJpa(idx, lastTwentyIdx);
    repo.saveTwenty(idx);

    JpaTwentyEven evenIdx = new JpaTwentyEven();
    populateJpa(evenIdx, lastEvenTwentyIdx);
    repo.saveEvenTwenty(evenIdx);
  }

  private void saveIndexEth() {
    JpaIdxEth idx = new JpaIdxEth();
    populateJpa(idx, lastIdxEth);
    repo.saveEth(idx);

    JpaIdxEthEven eIdx = new JpaIdxEthEven();
    populateJpa(eIdx, lastIdxEthEven);
    repo.saveEthEven(eIdx);
  }

  private void saveIndexForty() {
    JpaIdxForty idx = new JpaIdxForty();
    populateJpa(idx, lastIdxForty);
    repo.saveForty(idx);

    JpaIdxFortyEven eIdx = new JpaIdxFortyEven();
    populateJpa(eIdx, lastIdxFortyEven);
    repo.saveFortyEven(eIdx);
  }

  private void populateJpa(JpaIndex idx, IndexDbDto dto) {
    idx.setIndexValueBtc(dto.indexValueBtc)
        .setIndexValueUsd(dto.indexValueUsd)
        .setTimeStamp(dto.timeStamp);
  }

  public Collection<Index> getLatestCap() {
    return indexCalculatorTen.getSortedValues();
  }

  TickerService put(String name, double cap) {
    indexCalculatorTen.put(name, 0, cap);
    return this;
  }
}

