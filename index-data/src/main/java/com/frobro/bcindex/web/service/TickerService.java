package com.frobro.bcindex.web.service;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frobro.bcindex.core.db.domain.*;
import com.frobro.bcindex.core.db.service.*;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.model.BletchInEth;
import com.frobro.bcindex.web.model.BletchInTen;
import com.frobro.bcindex.web.model.BletchInTwenty;
import com.frobro.bcindex.web.service.apis.CoinCompare;
import com.frobro.bcindex.web.service.apis.HttpApi;
import com.frobro.bcindex.web.service.persistence.IndexDbDto;

/**
 * Created by rise on 3/23/17.
 */
public class TickerService {

  private static final BcLog log = BcLog.getLogger(TickerService.class);
  private final CoinCompare coinCompare = new CoinCompare();
  // calculators
  private IndexCalculator indexCalculatorTen = new IndexCalculatorTen();
  private IndexCalculator indexCalculatorTwenty = new IndexCalculatorTwenty();
  private IndexCalculator indexCalculatorEth = new IndexCalculatorEth();
  // repos
  PrimeRepo repo;
  // db data
  private IndexDbDto lastIndex;
  private IndexDbDto lastEvenIndex;
  private IndexDbDto lastTwentyIdx;
  private IndexDbDto lastEvenTwentyIdx;
  private IndexDbDto lastIdxEth;
  private IndexDbDto lastIdxEthEven;
  // input data
  private BletchInTen inputDataTen = new BletchInTen();
  private BletchInTwenty inputDataTwenty = new BletchInTwenty();
  private BletchInEth inputEth = new BletchInEth();

  public void setIndexRepo(IndexRepo repo, EvenIdxRepo eRepo,
                           TwentyRepo tRepo, TwentyEvenRepo teRepo,
                           EthRepo etRepo, EthEvenRepo eteRepo) {

    this.repo = PrimeRepo.getRepo(repo, eRepo,
                                  tRepo, teRepo,
                                  etRepo, eteRepo);
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

    // update 10 idx
    inputDataTen.setMembers(coinCompare.getData(inputDataTen.getMembers()));
    inputDataTen.setLastUpdate(System.currentTimeMillis());
    calculateAndSetIndexesTen(inputDataTen);

    // update 20 idx
    inputDataTwenty.setMembers(coinCompare.getData(inputDataTwenty.getMembers()));
    inputDataTwenty.setLastUpdate(System.currentTimeMillis());
    calculateAndSetIndexesTwenty(inputDataTwenty);

    // update ETH idx
    inputEth.setMembers(coinCompare.getData(inputEth.getMembers()));
    inputEth.setLastUpdate(System.currentTimeMillis());
    calculateAndSetIndexesEth(inputEth);

    logIndexSummary();
  }

  private void logIndexSummary() {
    String summary = "\nten      [btc=" + lastIndex.indexValueBtc + ", usd=" + lastIndex.indexValueUsd + "]\n"
                     + "ten even [btc=" + lastEvenIndex.indexValueBtc + ", usd=" + lastEvenIndex.indexValueUsd + "]\n"
        + "-----------------------------\n"
        + "twenty      [btc=" + lastTwentyIdx.indexValueBtc + ", usd=" + lastTwentyIdx.indexValueUsd + "]\n"
        + "twenty even [btc=" + lastEvenTwentyIdx.indexValueBtc + ", usd=" + lastEvenTwentyIdx.indexValueUsd + "]\n"
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

  private void calculateAndSetIndexesTen(BletchInTen data) {
    indexCalculatorTen.updateLast(data);
    lastIndex = indexCalculatorTen.calcuateOddIndex();
    lastEvenIndex = indexCalculatorTen.calculateEvenIndex();
  }

  public void updateTickerBtc() throws IOException {
    // get the value
    double btcPrice = coinCompare.getBtcPrice();
    // set the value
    inputDataTen.setLastUsdBtc(btcPrice);
    inputDataTwenty.setLastUsdBtc(btcPrice);
    inputEth.setLastUsdBtc(btcPrice);
  }

  public void saveIndices() {
    saveIndexTen();
    saveIndexTwenty();
    saveIndexEth();
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

