package com.frobro.bcindex.web;

import com.frobro.bcindex.web.service.TickerService;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by rise on 3/23/17.
 */
public class GetTickersTest {

  @Test
  public void testRestCall() throws IOException {
    TickerService service = new TickerService();

    service.updateTickers();
  }

  @Test
  public void testSum() throws IOException {
    TickerService service = new TickerService();
    double a = 10;
    double b = 20;
    service.put("a",a).put("b",b);

    Assert.assertEquals(30.0, service.getLatestSum(),0.001);

    Assert.assertEquals(b, service.getLatestCap().iterator().next().getMktCap(),0.001);
  }
}
