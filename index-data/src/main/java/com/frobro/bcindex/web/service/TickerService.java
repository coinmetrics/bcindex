package com.frobro.bcindex.web.service;

import java.io.IOException;
import java.util.*;

import com.frobro.bcindex.core.db.domain.*;
import com.frobro.bcindex.core.db.service.*;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.model.*;
import com.frobro.bcindex.web.service.apis.CryptoCompare;
import com.frobro.bcindex.web.service.persistence.IndexDbDto;
import com.frobro.bcindex.web.service.rules.*;

/**
 * Created by rise on 3/23/17.
 */
public class TickerService {

  private static final BcLog log = BcLog.getLogger(TickerService.class);
  private final CryptoCompare cryptoCompare = new CryptoCompare();
  // calculators
  private final IndexCalculator indexCalculatorTen = new IndexCalculatorTen();
  private final IndexCalculator indexCalculatorTwenty = new IndexCalculatorTwenty();
  private final IndexCalculator indexCalculatorEth = new IndexCalculatorEth();
  private final IndexCalculator indexCalculatorForty = new IndexCalculatorForty();
  private final IndexCalculator indexCalculatorTotal = new IndexCalculatorTotal();
  private final IndexCalculator indexCalculatorApp = new IndexCalculatorSector(new BusRulesApp());
  private final IndexCalculator indexCalculatorPlatform = new IndexCalculatorSector(new BusRulesPlatform());
  private final IndexCalculator indexCalculatorCurrency = new IndexCalculatorSector(new BusRulesCurrency());

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
  private IndexDbDto lastIdxTotal;
  private IndexDbDto lastIdxTotalEven;
  private IndexDbDto lastIdxApp;
  private IndexDbDto lastIdxPlatform;
  private IndexDbDto lastIdxCurrency;

  // input data
  private BletchleyData inputDataTen = newTen();
  private BletchleyData inputDataTwenty = newTwenty();
  private BletchleyData inputEth = newEth();
  private BletchleyData inputForty = newForty();
  private BletchleyData inputTotal = newTotal();
  private BletchleyData inputApp = newApp();
  private BletchleyData inputPlatform = newPlatform();
  private BletchleyData inputCurrency = newCurrency();

  public void setIndexRepo(IndexRepo repo, EvenIdxRepo eRepo,
                           TwentyRepo tRepo, TwentyEvenRepo teRepo,
                           EthRepo etRepo, EthEvenRepo eteRepo,
                           FortyIdxRepo fRepo, FortyEvenIdxRepo feRepo,
                           TotalRepo toRepo, TotalEvenRepo toeRepo,
                           CurrencyRepo currRepo, PlatformRepo pRepo,
                           ApplicationRepo aRepo) {

    this.repo = PrimeRepo.getRepo(repo, eRepo,
                                  tRepo, teRepo,
                                  etRepo, eteRepo,
                                  fRepo, feRepo,
                                  toRepo, toeRepo,
                                  currRepo,pRepo,aRepo);
  }

  public TickerService updateTickers() {
    try {

      update();
      saveIndices();

    } catch (Exception e) {
      log.error("could not successfully update. ", e);
    }
    return this;
  }

  private void update() throws IOException {
    log.info("updating latest data");

    clearInputData();

    // batch all api calls
    cryptoCompare.addBtcToBatch()
               .batchCoins(inputDataTen.getMembers())
               .batchCoins(inputDataTwenty.getMembers())
               .batchCoins(inputEth.getMembers())
               .batchCoins(inputForty.getMembers())
               .batchCoins(inputTotal.getMembers())
               .batchCoins(inputApp.getMembers())
               .batchCoins(inputPlatform.getMembers())
               .batchCoins(inputCurrency.getMembers());

    // call the api to get the data
    Map<String,Index> apiData = cryptoCompare.callBatchedData();

    // get bit coin latest
    // TODO: move this to batch call
    updateTickerBtc(cryptoCompare.extractBtcFromData(apiData));

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

    // update total idx
    inputTotal.filterAndSet(apiData);
    inputTotal.setLastUpdate(time);
    calculateAndSetIndexesTotal(inputTotal);

    inputApp.filterAndSet(apiData);
    inputApp.setLastUpdate(time);
    calculateAndSetIndexesApp(inputApp);

    inputPlatform.filterAndSet(apiData);
    inputPlatform.setLastUpdate(time);
    calculateAndSetIndexesPlatform(inputPlatform);

    inputCurrency.filterAndSet(apiData);
    inputCurrency.setLastUpdate(time);
    calculateAndSetIndexesCurrency(inputCurrency);

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
        + "eth even [btc=" + lastIdxEthEven.indexValueBtc + ", usd=" + lastIdxEthEven.indexValueUsd + "]\n"
        + "-----------------------------\n"
        + "total      [btc=" + lastIdxTotal.indexValueBtc + ", usd=" + lastIdxTotal.indexValueUsd + "]\n"
        + "total even [btc=" + lastIdxTotalEven.indexValueBtc + ", usd=" + lastIdxTotalEven.indexValueUsd + "]\n"
        + "-----------------------------\n"
        + "application [btc=" + lastIdxApp.indexValueBtc + ", usd=" + lastIdxApp.indexValueUsd + "]\n"
        + "-----------------------------\n"
        + "platform [btc=" + lastIdxPlatform.indexValueBtc + ", usd=" + lastIdxPlatform.indexValueUsd + "]\n"
        + "-----------------------------\n"
        + "currency [btc=" + lastIdxCurrency.indexValueBtc + ", usd=" + lastIdxCurrency.indexValueUsd + "]\n"
        ;
        log.debug(summary);
  }

  private void clearInputData() {
    inputDataTen = newTen();
    inputDataTwenty = newTwenty();
    inputEth = newEth();
    inputForty = newForty();
    inputTotal = newTotal();
    inputApp = newApp();
    inputPlatform = newPlatform();
    inputCurrency = newCurrency();
  }

  private BletchleyData newTen() {
    return new BletchleyData(new BusRulesTen());
  }

  private BletchleyData newTwenty() {
    return new BletchleyData(new BusRulesTwenty());
  }

  private BletchleyData newEth() {
    return new BletchleyData(new BusRulesEth());
  }

  private BletchleyData newForty() {
    return new BletchleyData(new BusRulesForty());
  }

  private BletchleyData newTotal() {
    return new BletchleyData(new BusRulesTotal());
  }

  private BletchleyData newApp() {
    return new BletchleyData(new BusRulesApp());
  }

  private BletchleyData newPlatform() {
    return new BletchleyData(new BusRulesPlatform());
  }

  private BletchleyData newCurrency() {
    return new BletchleyData(new BusRulesCurrency());
  }

  private void calculateAndSetIndexesTwenty(BletchleyData data) {
    indexCalculatorTwenty.updateLast(data);
    lastTwentyIdx = indexCalculatorTwenty.calcuateOddIndex();
    lastEvenTwentyIdx = indexCalculatorTwenty.calculateEvenIndex();
  }

  private void calculateAndSetIndexesEth(BletchleyData data) {
    indexCalculatorEth.updateLast(data);
    lastIdxEth = indexCalculatorEth.calcuateOddIndex();
    lastIdxEthEven = indexCalculatorEth.calculateEvenIndex();
  }

  private void calculateAndSetIndexesForty(BletchleyData data) {
    indexCalculatorForty.updateLast(data);
    lastIdxForty = indexCalculatorForty.calcuateOddIndex();
    lastIdxFortyEven = indexCalculatorForty.calculateEvenIndex();
  }

  private void calculateAndSetIndexesTotal(BletchleyData data) {
    indexCalculatorTotal.updateLast(data);
    lastIdxTotal = indexCalculatorTotal.calcuateOddIndex();
    lastIdxTotalEven = indexCalculatorTotal.calculateEvenIndex();
  }

  private void calculateAndSetIndexesTen(BletchleyData data) {
    indexCalculatorTen.updateLast(data);
    lastIndex = indexCalculatorTen.calcuateOddIndex();
    lastEvenIndex = indexCalculatorTen.calculateEvenIndex();
  }

  private void calculateAndSetIndexesApp(BletchleyData data) {
    indexCalculatorApp.updateLast(data);
    lastIdxApp = indexCalculatorApp.calcuateOddIndex();
  }

  private void calculateAndSetIndexesPlatform(BletchleyData data) {
    indexCalculatorPlatform.updateLast(data);
    lastIdxPlatform = indexCalculatorPlatform.calcuateOddIndex();
  }

  private void calculateAndSetIndexesCurrency(BletchleyData data) {
    indexCalculatorCurrency.updateLast(data);
    lastIdxCurrency = indexCalculatorCurrency.calcuateOddIndex();
  }

  public void updateTickerBtc(double btcPrice) throws IOException {
    inputDataTen.setLastUsdBtc(btcPrice);
    inputDataTwenty.setLastUsdBtc(btcPrice);
    inputEth.setLastUsdBtc(btcPrice);
    inputForty.setLastUsdBtc(btcPrice);
    inputTotal.setLastUsdBtc(btcPrice);
    inputApp.setLastUsdBtc(btcPrice);
    inputPlatform.setLastUsdBtc(btcPrice);
    inputCurrency.setLastUsdBtc(btcPrice);
  }

  public void saveIndices() {
    saveIndexTen();
    saveIndexTwenty();
    saveIndexEth();
    saveIndexForty();
    saveIndexTotal();
    saveSector();
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

  private void saveIndexTotal() {
    JpaIndexTotal idx = new JpaIndexTotal();
    populateJpa(idx, lastIdxTotal);
    repo.saveTotal(idx);

    JpaIndexTotalEven eIdx = new JpaIndexTotalEven();
    populateJpa(eIdx, lastIdxTotalEven);
    repo.saveTotalEven(eIdx);
  }

  private void saveSector() {
    JpaCurrency idx = new JpaCurrency();
    populateJpa(idx, lastIdxCurrency);
    repo.saveCurrency(idx);

    JpaPlatform pIdx = new JpaPlatform();
    populateJpa(pIdx, lastIdxPlatform);
    repo.savePlatform(pIdx);

    JpaApplication aIdx = new JpaApplication();
    populateJpa(aIdx, lastIdxApp);
    repo.saveAplication(aIdx);
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

