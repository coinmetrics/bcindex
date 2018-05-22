package com.frobro.bcindex.web.service.publish;

public abstract class PublishService {
  protected HttpPublisher httpPublisher;

  abstract public String publishEndPtKey();

  // dynamically set the host depending on env (dev, stage, prod, ect)
  public void createPublishEndPoint(String endPoint) {
    httpPublisher = new HttpPublisher(endPoint);
  }
}
