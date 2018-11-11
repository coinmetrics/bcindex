package com.frobro.bcindex.web.service.publish;

import com.frobro.bcindex.core.model.IndexPrice;

import java.util.Map;

public class PricePublishService extends PublishService {

  public void publish(Map<String,IndexPrice> data) {
    httpPublisher.publish(data);
  }

  @Override
  public String publishEndPtKey() {
    return "priceEndPoint";
  }
}
