package com.frobro.bcindex.web.service.query;

import java.util.LinkedList;
import java.util.List;

public class JsonHolder {
  private JsonRow first = new JsonRow();
  private JsonRow last =  new JsonRow();
  private LinkedList<JsonRow> body = new LinkedList<>();

  public JsonHolder firstEpochTime(String t) {
    if (first.time == null) {
      first.time = t;
    }
    return this;
  }

  public JsonHolder firstBtcPx(String p) {
    if (first.pxBtc == null) {
      first.pxBtc = p;
    }
    return this;
  }

  public JsonHolder firstUsdPx(String p) {
    if (first.pxUsd == null) {
      first.pxUsd = p;
    }
    return this;
  }

  public JsonHolder lastEpochTime(String t) {
    if (last.time == null) {
      last.time = t;
    }
    return this;
  }

  public JsonHolder lastBtcPx(String p) {
    if (last.pxBtc == null) {
      last.pxBtc = p;
    }
    return this;
  }

  public JsonHolder lastUsdPx(String p) {
    if (last.pxUsd == null) {
      last.pxUsd = p;
    }
    return this;
  }

  public JsonHolder body(JsonRow json) {
    body.add(json);
    return this;
  }

  public List<JsonRow> build() {
    body.addFirst(first);
    body.addLast(last);
    return body;
  }
}
