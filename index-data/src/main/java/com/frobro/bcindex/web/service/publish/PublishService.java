package com.frobro.bcindex.web.service.publish;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frobro.bcindex.core.db.model.WeightApi;
import com.frobro.bcindex.web.service.apis.HttpApi;

import java.io.IOException;

public abstract class PublishService {
  private String endPoint;

  abstract public String publishEndPtKey();

  // dynamically set the host depending on env (dev, stage, prod, ect)
  public void createPublishEndPoint(String endPoint) {
    this.endPoint = endPoint;
  }

  public void publish(WeightApi data) {
    data.addKey();
    publish(data.getRawData());
  }

  public void publish(Object data) {
    HttpApi api = new HttpApi();
    try {

      api.publish(endPoint, toString(data));

    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  private String toString(Object data) throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(data);
  }

}
