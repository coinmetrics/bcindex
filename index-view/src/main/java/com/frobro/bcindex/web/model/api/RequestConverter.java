package com.frobro.bcindex.web.model.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frobro.bcindex.core.service.BletchDate;

import java.io.IOException;
import java.util.stream.Collectors;

public class RequestConverter {

  public static RequestDto convert(PublicRequest pubReq) {
    RequestDto privateReq = new RequestDto();

    // set currency
    privateReq.currency = pubReq.currency;

    // set index
    privateReq.index = pubReq.index.getPrivateIdx();

    // set time frame
    if (pubReq.timeFrame == PublicTimeFrame.DAILY) {
      privateReq.timeFrame = TimeFrame.DAILY;
    }
    else if (pubReq.timeFrame == PublicTimeFrame.WEEKLY) {
      privateReq.timeFrame = TimeFrame.WEEKLY;
    }

    return privateReq;
  }

  public static PublicRequest convert(RequestDto internalReq) {
    PublicRequest pubReq = new PublicRequest();
    pubReq.index = PublicIndex.getPublicIdx(internalReq.index);
    pubReq.timeFrame = convert(internalReq.timeFrame);
    pubReq.currency = internalReq.currency;
    return pubReq;
  }

  public static PublicRequest convert(String json) throws IOException {
      return new ObjectMapper().readValue(json, PublicRequest.class);
  }

  public static PublicApiResponse convert(ApiResponse privateResp) {
    PublicApiResponse publicResp = new PublicApiResponse();
    publicResp.currency = privateResp.currency;
    publicResp.timeFrame = convert(privateResp.timeFrame);
    publicResp.index = convert(privateResp.index);

    publicResp.lastPrice = privateResp.lastPrice;
    publicResp.high = privateResp.high;
    publicResp.low = privateResp.low;
    publicResp.prevClose = privateResp.prevClose;
    publicResp.change = privateResp.change;
    publicResp.percentChange = privateResp.percentChange;

    publicResp.data = privateResp.data;
    publicResp.times = privateResp.times.stream().map(k -> BletchDate.toEpochMilli(k)).collect(Collectors.toList());

    return publicResp;
  }

  private static PublicTimeFrame convert(TimeFrame privateFrame) {
    PublicTimeFrame pFrame = PublicTimeFrame.DAILY;

    if (TimeFrame.WEEKLY == privateFrame) {
      pFrame = PublicTimeFrame.WEEKLY;
    }

    return pFrame;
  }

  private static PublicIndex convert(IndexType privateIdx) {
    return PublicIndex.getPublicIdx(privateIdx);
  }
}
