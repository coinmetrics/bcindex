package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.service.BusinessRules;
import com.frobro.bcindex.web.service.TickerService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
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
  }

  @Test
  public void testSum() throws IOException {
    // given
    TickerService service = new TickerService();
    double a = 10;
    double b = 20;
    service.init();

    // when
    service.put("a",a).put("b",b);

    // then
    double expectedIndexValue = -1*(a+ b + service.getConstant()) / (new BusinessRules().getDivisor());
    Assert.assertEquals(expectedIndexValue, service.getIndexValue(),0.001);
    Assert.assertEquals(b, service.getLatestCap().iterator().next().getMktCap(),0.001);
  }
}
