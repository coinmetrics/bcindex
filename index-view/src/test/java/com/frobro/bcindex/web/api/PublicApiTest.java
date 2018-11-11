package com.frobro.bcindex.web.api;

import com.frobro.bcindex.web.model.api.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PublicApiTest {

  @Test
  public void testCombosOfRequests() {
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

  @Test
  public void testResponseConversion() {
    RequestDto dto = new RequestDto();
    dto.currency = Currency.USD;
    dto.timeFrame = TimeFrame.DAILY;
    dto.index = IndexType.ODD_INDEX;
    ApiResponse resp = ApiResponse.newResponse(dto);

    PublicApiResponse publicResp = RequestConverter.convert(resp);

    assertEquals(resp.change, publicResp.change);
    assertEquals(resp.currency, publicResp.currency);
    assertEquals(PublicIndex.TEN_INDEX, publicResp.index);
    // add verify summary
    // add verify data and times
  }
}
