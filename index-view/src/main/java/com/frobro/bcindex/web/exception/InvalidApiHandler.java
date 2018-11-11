package com.frobro.bcindex.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InvalidApiHandler {
  @ExceptionHandler(ApiKeyInvalidException.class)
  public ResponseEntity<String> resourceNotFound(ApiKeyInvalidException ex) {
    return new ResponseEntity<>(ex.getErrorMsg(), HttpStatus.FORBIDDEN);
  }
}
