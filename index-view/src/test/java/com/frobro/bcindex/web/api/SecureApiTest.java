package com.frobro.bcindex.web.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frobro.bcindex.web.controller.ApiController;
import com.frobro.bcindex.web.exception.ApiKeyInvalidException;
import com.frobro.bcindex.web.framework.MockApiController;
import com.frobro.bcindex.web.model.api.*;
import org.junit.Test;

public class SecureApiTest {

  @Test(expected = ApiKeyInvalidException.class)
  public void testSecureEndPointWithInvalidKey() {
    // when
    callEndPoint("invalidKey");
    // then expect exception
  }

  @Test(expected = ApiKeyInvalidException.class)
  public void testKeyNotPresent() {
    // given
    ApiController controller = new MockApiController();
    String json = generateJson(new PublicRequest());

    // when
    controller.publicProtectedApiEndPoint(json);
  }

  @Test
  public void testWithValidKey() {
    // when
    callEndPoint("abcnomics");
    // then no exception is thrown
  }

  private void callEndPoint(String apiKey) {
    ApiController controller = new MockApiController();
    String json = generateJson(apiKey);

    // when
    controller.publicProtectedApiEndPoint(json);

  }

  private String generateJson(String apiKey) {
    SecurePublicRequest req = new SecurePublicRequest();
    req.apiKey = apiKey;

    return generateJson(req);
  }

  private String generateJson(PublicRequest req) {
    req.currency = Currency.USD;
    req.index = PublicIndex.TEN_INDEX;
    req.timeFrame = PublicTimeFrame.DAILY;

    try {

      return new ObjectMapper().writeValueAsString(req);

    } catch (JsonProcessingException jpe) {
      throw new RuntimeException(jpe);
    }
  }
}
