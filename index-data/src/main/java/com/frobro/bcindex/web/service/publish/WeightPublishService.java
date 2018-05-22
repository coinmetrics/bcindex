package com.frobro.bcindex.web.service.publish;

import com.frobro.bcindex.core.model.WeightApi;

public class WeightPublishService extends PublishService {
  @Override
  public String publishEndPtKey() {
    return "weightEndPoint";
  }

  public void publish(WeightApi data) {
    httpPublisher.publish(data.getWeightDto());
  }
}
