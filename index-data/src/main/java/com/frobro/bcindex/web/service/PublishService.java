package com.frobro.bcindex.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frobro.bcindex.core.db.model.WeightApi;
import com.frobro.bcindex.web.service.apis.HttpApi;

import java.io.IOException;

public class PublishService {
  private static final String DEFAULT_BASE_END_POINT = "localhost:8090";
  private static final String HTTPS = "http://";
  private static final String WEIGHT_URL = "/blet/weight";
  private static String weightEndPoint = HTTPS + DEFAULT_BASE_END_POINT + WEIGHT_URL;

  public static String publichHostKey() {
    return "publishHost";
  }

  // dynamically set the host depending on env (dev, stage, prod, ect)
  public static void createPublishEndPoint(String host) {
    weightEndPoint = HTTPS + host + WEIGHT_URL;
  }

  public void publish(WeightApi data) {
    HttpApi api = new HttpApi();
    try {

      data.addKey();
      api.publish(weightEndPoint, toString(data));

    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  public String toString(WeightApi data) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(data.getRawData());
  }
}
