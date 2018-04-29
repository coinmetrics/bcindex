package com.frobro.bcindex.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

  @RequestMapping("/status")
  public String healthCheck() {
    return "app is up";
  }
}
