package com.frobro.bcindex.web.service.query;

import java.util.LinkedList;
import java.util.List;

public class JsonHolder {
  private LinkedList<JsonRow> body = new LinkedList<>();


  public JsonHolder body(JsonRow json) {
    body.add(json);
    return this;
  }

  public List<JsonRow> build() {
    return body;
  }
}
