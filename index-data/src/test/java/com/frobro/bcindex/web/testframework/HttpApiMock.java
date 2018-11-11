package com.frobro.bcindex.web.testframework;

import com.frobro.bcindex.web.service.apis.HttpApi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HttpApiMock extends HttpApi {

  @Override
  public String makeApiCall(String endpoint) throws IOException {
    return new String(Files.readAllBytes(Paths.get("src/test/resources/test_data/apiTest.json")));
  }
}
