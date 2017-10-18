package com.frobro.bcindex.web.service.time;

import com.frobro.bcindex.web.model.api.ApiResponse;
import com.frobro.bcindex.web.model.api.RequestDto;

public class BletchClock {
  public long getTimeEpochMillis() {
    return System.currentTimeMillis();
  }
}