package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.model.BletchleyData;
import com.frobro.bcindex.web.service.persistence.IndexDbDto;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by rise on 3/23/17.
 */
public class GetTickersTest {

  @Ignore
  @Test
  public void testRestCall() throws IOException {
    TickerService service = new TickerService();
    service.updateTickers();
//    String res = service.makeApiCallBtc();
//    System.out.println(res);
//    service.updateTickerBtc(res);
  }

  @Ignore
  @Test
  public void testSum() throws IOException {
    // given
    TickerService service = new TickerService();
    double a = 10;
    double b = 20;
    service.init();

    // when
    service.put("a",a).put("b", b);

    // then
//   double expectedIndexValue = -1*(a+ b + service.getConstant()) / (new BusRulesTen().getDivisor());
//    Assert.assertEquals(expectedIndexValue, service.getIndexValue(),0.001);
    assertEquals(b, service.getLatestCap().iterator().next().getMktCap(), 0.001);
  }

  private void turnOnConsoleLogging() {
    System.setProperty(BcLog.getLogKey(), "true");
  }

  @Test
  public void testCalcOdd() {
    // given
    turnOnConsoleLogging();
    // and
    double btcPrice = 2674.0;
    // and
    BletchleyData input = new BletchleyData();
    input.setLastUpdate(System.currentTimeMillis());
    input.setLastUsdBtc(btcPrice);
    input.setMembers(populateIndices(btcPrice));
    // and expects are calculated
    double expectOddIdxBtc = calcOdd(input, btcPrice);
    double expectOddIdxUsd = expectOddIdxBtc * btcPrice;
    long expectTime = input.getTimeStamp();
    double expectEvenIdxBtc = calcEven(input, btcPrice);
    double epectEvenIdxUsd = expectEvenIdxBtc * btcPrice;

    // when
    IndexCalculator calculator = new IndexCalculatorTen();
    calculator.updateLast(input);
    IndexDbDto odd = calculator.calcuateOddIndex();
    IndexDbDto even = calculator.calculateEvenIndex();

    // then
    double precision = 0.001;
    assertEquals(expectOddIdxBtc, odd.indexValueBtc, precision);
    assertEquals(expectOddIdxUsd, odd.indexValueUsd, precision);
    assertEquals(expectTime,odd.timeStamp);
    assertEquals(expectEvenIdxBtc, even.indexValueBtc, precision);
    assertEquals(epectEvenIdxUsd, even.indexValueUsd, precision);
  }

  private double calcEven(BletchleyData input, double btcPrice) {
    double sum = 0;
    double constant = 3575708.58522349;
    double divisor = 9938260.33786684;

    for (Index idx : input.getLastIndexes().values()) {
      double mktCap = idx.getEvenMult() * btcPrice;
      sum += idx.getEvenMult();
    }

    double result = (sum + constant) / divisor;
    return result;
  }

  private double calcOdd(BletchleyData input, double btcPrice) {
    double sum = 0;
    double constant = 16399912;
    double divisor = 22231031.0380042;

    for (Index idx : input.getLastIndexes().values()) {
      double mult = idx.getMktCap();
      sum += mult;
    }
    double result = (sum + constant) / divisor;
    return result;
  }

  private Map<String, Index> populateIndices(double btcPrice) {
    String rebalance = getJuneRebalance();
    Map<String,Double> prices = getLastPxMap();

    Map<String,Index> map = new HashMap<>();
    for (String line : rebalance.split("\n")) {
      String[] data = line.split(",");
      Index idx = new Index()
          .setLast(prices.get((data[0])))
          .setName(data[0])
          .setMktCap(Double.parseDouble(data[1]))
          .setEvenMult(Double.parseDouble(data[2]));

      map.put(idx.getName(),idx);
    }
    return map;
  }

  private Map<String,Double> getLastPxMap() {
    Map<String,Double> map = new HashMap<>();
    map.put("BTC_XMR", 0.0185208);
    map.put("BTC_LTC", 0.016799);
    map.put("BTC_ETH", 0.12213898);
    map.put("BTC_XRP", 0.0001046);
    map.put("BTC_DASH", 0.06810908);
    map.put("BTC_ETC", 0.00723252);
    map.put("BTC_STRAT", 0.00286787);
    map.put("BTC_BTS", 0.00011269);
    map.put("BTC_XEM", 0.00007376);
    return map;
  }

  private String getJuneRebalance() {
    return "BTC_ETH,92624447,28468854\n" +
        "BTC_DASH,7372195,52661401\n" +
        "BTC_XMR,14652614,188030316\n" +
        "BTC_STRAT,98430749,1252428559\n" +
        "BTC_ETC,92756313,479960884\n" +
        "BTC_LTC,51624457,222009586\n" +
        "BTC_XRP,38290271363,32895203176\n" +
        "BTC_XEM,9000000000,47454659392\n" +
        "BTC_BTS,2596210000,30899659395\n";
  }

  private String getMayRebalance() {
    return "BTC_ETH,91664151,59873526.51\n" +
        "BTC_DASH,7299984,61895790.42\n" +
        "BTC_XMR,14471420,197309610.3\n" +
        "BTC_STR,9557363131,112087148843\n" +
        "BTC_ETC,91684893,849147452.2\n" +
        "BTC_LTC,51134482,219046323.4\n" +
        "BTC_XRP,38305873865,17167303108\n" +
        "BTC_XEM,9000000000,48757909747\n" +
        "BTC_REP,11000000,344333951.9";
  }
}
