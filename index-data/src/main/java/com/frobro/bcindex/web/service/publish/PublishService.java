package com.frobro.bcindex.web.service.publish;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frobro.bcindex.core.db.model.IndexPrice;
import com.frobro.bcindex.core.db.model.WeightApi;
import com.frobro.bcindex.web.bclog.BcLog;
import com.frobro.bcindex.web.service.apis.HttpApi;

import java.io.IOException;
import java.util.Map;

public abstract class PublishService {
  private static final BcLog LOG = BcLog.getLogger(PublishService.class);

  private String endPoint;

  abstract public String publishEndPtKey();

  // dynamically set the host depending on env (dev, stage, prod, ect)
  public void createPublishEndPoint(String endPoint) {
    this.endPoint = endPoint;
  }

  public void publish(Object data) {
    HttpApi api = new HttpApi();
    try {

      LOG.debug("publishing daily to: " + endPoint);
      api.publish(endPoint, toString(data));

    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  private String toString(Object data) throws JsonProcessingException {
    return new ObjectMapper().writeValueAsString(data);
  }

}
