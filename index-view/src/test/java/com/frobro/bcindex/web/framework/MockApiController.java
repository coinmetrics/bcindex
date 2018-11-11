package com.frobro.bcindex.web.framework;

import com.frobro.bcindex.web.controller.ApiController;
import com.frobro.bcindex.web.model.api.RequestDto;

public class MockApiController extends ApiController {

  @Override
  protected String processRequest(RequestDto req) {
    return "mock-dat";
  }
}
