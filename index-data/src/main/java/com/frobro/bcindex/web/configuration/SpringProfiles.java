package com.frobro.bcindex.web.configuration;

/**
 * Created by rise on 5/20/17.
 */
public enum SpringProfiles {
  DEV, POSTGRES;

  public String getValue() {
    return name().toLowerCase();
  }
}
