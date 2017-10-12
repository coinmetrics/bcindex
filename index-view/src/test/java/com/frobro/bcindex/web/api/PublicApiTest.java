package com.frobro.bcindex.web.api;

import static org.junit.Assert.assertEquals;
import com.frobro.bcindex.web.model.api.*;
import org.junit.Test;

public class PublicApiTest {

  @Test
  public void testLoopCombos() {
    // given
    PublicRequest pubReq = new PublicRequest();
    pubReq.timeFrame = PublicTimeFrame.DAILY;
    pubReq.currency = Currency.USD;

    for (PublicIndex idx : PublicIndex.values()) {
      pubReq.index = idx;

      RequestDto privateReq = RequestConverter.convert(pubReq);
      PublicRequest converted = RequestConverter.convert(privateReq);

      assertEquals(pubReq, converted);
    }
  }
}
