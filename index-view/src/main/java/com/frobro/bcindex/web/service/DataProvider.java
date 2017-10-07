package com.frobro.bcindex.web.service;

import com.frobro.bcindex.web.model.api.ApiResponse;
import com.frobro.bcindex.web.model.api.RequestDto;

public interface DataProvider {
  ApiResponse getData(RequestDto req);
}