package com.frobro.bcindex.web.service;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.frobro.bcindex.core.db.domain.*;
import com.frobro.bcindex.core.db.service.*;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.model.BletchInEth;
import com.frobro.bcindex.web.model.BletchInTen;
import com.frobro.bcindex.web.model.BletchInTwenty;
import com.frobro.bcindex.web.service.persistence.IndexDbDto;
import com.frobro.bcindex.web.model.BletchleyData;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Created by rise on 3/23/17.
 */
public class TickerService {

  private static final BcLog log = BcLog.getLogger(TickerService.class);
  private static final String EMPTY_RESPONSE = "noop";
  private final static String COIN_MARKET_CAP_BTC_ENDPOINT = "https://api.coinmarketcap.com/v1/ticker/bitcoin/";
  private final static String POLONIEX_ENDPOINT = "https://poloniex.com/public?command=returnTicker";
  private static final String COIN_CAP_ENDPOINT_20 = "http://www.coincap.io/front";
  private static final String COIN_CAP_ENDPOINT_ETH = COIN_CAP_ENDPOINT_20;
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
    String btcResponse = makeApiCallBtc();
    updateTickerBtc(btcResponse);

    // update 10 idx
    String response = makeCallTenMembers();
    inputDataTen.setMembers(response);
    inputDataTen.setLastUpdate(System.currentTimeMillis());
    calculateAndSetIndexesTen(inputDataTen);

    // update 20 idx
    String response20 = makeApiCallTwenty();
    inputDataTwenty.setMembers(response20);
    inputDataTwenty.setLastUpdate(System.currentTimeMillis());
    calculateAndSetIndexesTwenty(inputDataTwenty);

    // update ETH idx
    String responseEth = response20; // right now these calls are the same
    inputEth.setMembers(responseEth);
    inputEth.setLastUpdate(System.currentTimeMillis());
    calculateAndSetIndexesEth(inputEth);
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

  private String makeApiCallEth() throws IOException {
    return makeApiCall(COIN_CAP_ENDPOINT_ETH);
  }

  private String makeApiCallTwenty() throws IOException {
    return makeApiCall(COIN_CAP_ENDPOINT_20);
  }

  public void updateTickerBtc(String response) throws IOException {
    // get the value
    double btcPrice = getBtcPrice(response);
    // set the value
    inputDataTen.setLastUsdBtc(btcPrice);
    inputDataTwenty.setLastUsdBtc(btcPrice);
    inputEth.setLastUsdBtc(btcPrice);
  }

  private double getBtcPrice(String json) {
    try {

      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(json);
      JsonNode node = root.get(0);
      return node.get("price_usd").asDouble();

    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  public String makeApiCallBtc() throws IOException {
    return makeApiCall(COIN_MARKET_CAP_BTC_ENDPOINT);
  }
  
  private String makeCallTenMembers() throws IOException {
    return makeApiCall(COIN_CAP_ENDPOINT_20);
  }

  private String makeApiCall(String endpoint) throws IOException {
    String response = EMPTY_RESPONSE;
    CloseableHttpClient httpClient = HttpClients.createDefault();
    try {

      HttpGet getRequest = new HttpGet(
          endpoint);
      getRequest.addHeader("accept", "application/json");
      response = httpClient.execute(getRequest, createResponseHandler());

    } finally {
      httpClient.close();
    }
    return response;
  }
  private ResponseHandler<String> createResponseHandler() {
    return new ResponseHandler<String>() {

      @Override
      public String handleResponse(
          final HttpResponse response) throws ClientProtocolException, IOException {

        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
          HttpEntity entity = response.getEntity();
          return entity != null ? EntityUtils.toString(entity) : null;
        } else {
          throw new ClientProtocolException("Unexpected response status: " + status);
        }
      }
    };
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

