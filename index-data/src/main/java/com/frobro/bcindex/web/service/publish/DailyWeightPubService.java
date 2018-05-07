package com.frobro.bcindex.web.service.publish;

import com.frobro.bcindex.core.db.model.WeightApi;

public class DailyWeightPubService extends PublishService {
  private long publishTime = ;


  @Override
  public String publishEndPtKey() {
    return "dailyWeightEndPoint";
  }

  @Override
  public void publishWeight(WeightApi data) {
    data.addKey();
    publish(data);
  }
}
