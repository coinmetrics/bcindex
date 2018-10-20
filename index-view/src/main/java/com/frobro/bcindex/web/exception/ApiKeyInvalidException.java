package com.frobro.bcindex.web.exception;

public class ApiKeyInvalidException extends RuntimeException {
  private final String errorMsg;

  public ApiKeyInvalidException(String msg) {
    super(msg);
    errorMsg = msg;
  }

  public String getErrorMsg() {
    return errorMsg;
  }
}
