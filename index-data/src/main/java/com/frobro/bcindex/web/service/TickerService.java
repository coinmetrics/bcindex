package com.frobro.bcindex.web.service;

import static com.frobro.bcindex.core.model.IndexName.*;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.frobro.bcindex.core.db.domain.*;
import com.frobro.bcindex.core.model.IndexName;
import com.frobro.bcindex.core.model.IndexPrice;
import com.frobro.bcindex.core.model.WeightApi;
import com.frobro.bcindex.core.db.service.*;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.model.*;
import com.frobro.bcindex.web.service.apis.CryptoCompare;
import com.frobro.bcindex.web.service.persistence.IndexDbDto;
import com.frobro.bcindex.web.service.publish.DailyWeightPubService;
import com.frobro.bcindex.web.service.publish.PricePublishService;
import com.frobro.bcindex.web.service.publish.WeightPublishService;
import com.frobro.bcindex.web.service.rules.*;

/**
 * Created by rise on 3/23/17.
 */
public class TickerService {

  private static final BcLog log = BcLog.getLogger(TickerService.class);
  private final CryptoCompare cryptoCompare = new CryptoCompare();
  private long updateTime;

  // publishers
  private WeightPublishService weightPublishService;
  private PricePublishService pricePublishService;
  private DailyWeightPubService dailyWeightPubService;

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

  public TickerService() {}

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

  public void setWeightPublisher(WeightPublishService publisher) {
    this.weightPublishService = publisher;
  }

  public void setDailyPxPublisher(PricePublishService publisher) {
    this.pricePublishService = publisher;
  }

  public void setDailyWeightPublisher(DailyWeightPubService publisher) {
    this.dailyWeightPubService = publisher;
  }

  public TickerService updateTickers() {
    long start = System.currentTimeMillis();
    try {

      update();
      saveIndices();
      publishWeights();
//      publishPrice();

      log.debug("update finished in "
          + TimeUnit.MILLISECONDS.toSeconds((System.currentTimeMillis() - start)) + " seconds");

    } catch (Exception e) {
      log.error("could not successfully update. ", e);
    }
    return this;
  }

  private void publishPrice() {
    Map<String,IndexPrice> prices = new HashMap<>();
    IndexPrice ten = new IndexPrice();
    ten.indexName = IndexName.TEN.name();
    ten.pxBtc = lastIndex.indexValueBtc;
    ten.pxUsd = lastIndex.indexValueUsd;
    ten.pxEvenBtc = lastEvenIndex.indexValueBtc;
    ten.pxEvenUsd = lastEvenIndex.indexValueUsd;
    ten.timeStamp = updateTime;
    prices.put(ten.indexName, ten);

    IndexPrice twenty = new IndexPrice();
    twenty.indexName = IndexName.TWENTY.name();
    twenty.pxBtc = lastTwentyIdx.indexValueBtc;
    twenty.pxUsd = lastTwentyIdx.indexValueUsd;
    twenty.pxEvenBtc = lastEvenTwentyIdx.indexValueBtc;
    twenty.pxEvenUsd = lastEvenTwentyIdx.indexValueUsd;
    twenty.timeStamp = updateTime;
    prices.put(twenty.indexName, twenty);

    IndexPrice forty = new IndexPrice();
    forty.indexName = IndexName.FORTY.name();
    forty.pxBtc = lastIdxForty.indexValueBtc;
    forty.pxUsd = lastIdxForty.indexValueUsd;
    forty.pxEvenBtc = lastIdxFortyEven.indexValueBtc;
    forty.pxEvenUsd = lastIdxFortyEven.indexValueUsd;
    forty.timeStamp = updateTime;
    prices.put(forty.indexName, forty);

    IndexPrice total = new IndexPrice();
    total.indexName = IndexName.TOTAL.name();
    total.pxBtc = lastIdxTotal.indexValueBtc;
    total.pxUsd = lastIdxTotal.indexValueUsd;
    total.pxEvenBtc = lastIdxTotalEven.indexValueBtc;
    total.pxEvenUsd = lastIdxTotalEven.indexValueUsd;
    total.timeStamp = updateTime;
    prices.put(total.indexName, total);

    IndexPrice ethereum = new IndexPrice();
    ethereum.indexName = IndexName.ETHEREUM.name();
    ethereum.pxBtc = lastIdxEth.indexValueBtc;
    ethereum.pxUsd = lastIdxEth.indexValueUsd;
    ethereum.pxEvenBtc = lastIdxEthEven.indexValueBtc;
    ethereum.pxEvenUsd = lastIdxEthEven.indexValueUsd;
    ethereum.timeStamp = updateTime;
    prices.put(ethereum.indexName, ethereum);

    IndexPrice currency = new IndexPrice();
    currency.indexName = IndexName.CURRENCY.name();
    currency.pxBtc = lastIdxCurrency.indexValueBtc;
    currency.pxUsd = lastIdxCurrency.indexValueUsd;
    currency.timeStamp = updateTime;
    prices.put(currency.indexName, currency);

    IndexPrice platform = new IndexPrice();
    platform.indexName = IndexName.PLATFORM.name();
    platform.pxBtc = lastIdxPlatform.indexValueBtc;
    platform.pxUsd = lastIdxPlatform.indexValueUsd;
    platform.timeStamp = updateTime;
    prices.put(platform.indexName, platform);

    IndexPrice application = new IndexPrice();
    application.indexName = IndexName.APPLICATION.name();
    application.pxBtc = lastIdxTotal.indexValueBtc;
    application.pxUsd = lastIdxTotal.indexValueUsd;
    application.timeStamp = updateTime;
    prices.put(application.indexName, application);

    pricePublishService.publish(prices);
  }

  private void publishWeights() {
    WeightApi data = new WeightApi();

    data.setTime(updateTime);
    data.addIndex(TEN, indexCalculatorTen.getWeights());
    data.addIndex(TEN_EVEN, indexCalculatorTen.getWeightsEven());

    data.addIndex(TWENTY, indexCalculatorTwenty.getWeights());
    data.addIndex(TWENTY_EVEN, indexCalculatorTwenty.getWeightsEven());

    data.addIndex(FORTY, indexCalculatorForty.getWeights());
    data.addIndex(FORTY_EVEN, indexCalculatorForty.getWeightsEven());

    data.addIndex(TOTAL, indexCalculatorTotal.getWeights());
    data.addIndex(TOTAL_EVEN, indexCalculatorTotal.getWeightsEven());

    data.addIndex(ETHEREUM, indexCalculatorEth.getWeights());
    data.addIndex(ETHEREUM_EVEN, indexCalculatorEth.getWeightsEven());

    data.addIndex(CURRENCY, indexCalculatorCurrency.getWeights());
    data.addIndex(PLATFORM, indexCalculatorPlatform.getWeights());
    data.addIndex(APPLICATION, indexCalculatorApp.getWeights());

    weightPublishService.publish(data);
    dailyWeightPubService.publish(data);
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

    updateTime = System.currentTimeMillis();

    // update 10 idx
    inputDataTen.filterAndSet(apiData);
    inputDataTen.setLastUpdate(updateTime);
    calculateAndSetIndexesTen(inputDataTen);

    // update 20 idx
    inputDataTwenty.filterAndSet(apiData);
    inputDataTwenty.setLastUpdate(updateTime);
    calculateAndSetIndexesTwenty(inputDataTwenty);

    // update ETH idx
    inputEth.filterAndSet(apiData);
    inputEth.setLastUpdate(updateTime);
    calculateAndSetIndexesEth(inputEth);

    // update forty idx
    inputForty.filterAndSet(apiData);
    inputForty.setLastUpdate(updateTime);
    calculateAndSetIndexesForty(inputForty);

    // update total idx
    inputTotal.filterAndSet(apiData);
    inputTotal.setLastUpdate(updateTime);
    calculateAndSetIndexesTotal(inputTotal);

    inputApp.filterAndSet(apiData);
    inputApp.setLastUpdate(updateTime);
    calculateAndSetIndexesApp(inputApp);

    inputPlatform.filterAndSet(apiData);
    inputPlatform.setLastUpdate(updateTime);
    calculateAndSetIndexesPlatform(inputPlatform);

    inputCurrency.filterAndSet(apiData);
    inputCurrency.setLastUpdate(updateTime);
    calculateAndSetIndexesCurrency(inputCurrency);

    logIndexSummary();
  }

  private void logIndexSummary() {
    LogService logService = new LogService();
    logService.addAllWeights("Ten",indexCalculatorTen)
              .addAllWeights("Twenty",indexCalculatorTwenty)
              .addAllWeights("Forty",indexCalculatorForty)
              .addAllWeights("Total",indexCalculatorTotal)
              .addAllWeights("Ethereum",indexCalculatorEth)
              .addSingleWeights("Currency",indexCalculatorCurrency.getWeights())
              .addSingleWeights("Platform",indexCalculatorPlatform.getWeights())
              .addSingleWeights("Application",indexCalculatorApp.getWeights())

              .addIndexValue("Ten",lastIndex, lastEvenIndex)
              .addIndexValue("Twenty",lastTwentyIdx, lastEvenTwentyIdx)
              .addIndexValue("Forty",lastIdxForty, lastIdxFortyEven)
              .addIndexValue("Total",lastIdxTotal, lastIdxTotalEven)
              .addIndexValue("Ethereum",lastIdxEth, lastIdxEthEven)
              .addIndexValue("Currency",lastIdxCurrency)
              .addIndexValue("Platform",lastIdxPlatform)
              .addIndexValue("Application",lastIdxApp);

    log.debug(logService.getLogMessage());
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

  /* get rid of these */
  private void calculateAndSetIndexesTwenty(BletchleyData data) {
    indexCalculatorTwenty.updateLast(data);
    // multiples will completed for the index weights at this point
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

