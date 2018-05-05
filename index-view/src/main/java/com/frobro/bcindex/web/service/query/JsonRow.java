package com.frobro.bcindex.web.service.query;

public class JsonRow {
  public String time;
  public String pxBtc;
  public String pxUsd;

  @Override
  public String toString() {
    return "JsonRow{" +
        "time='" + time + '\'' +
        ", pxBtc='" + pxBtc + '\'' +
        ", pxUsd='" + pxUsd + '\'' +
        '}';
  }
}
