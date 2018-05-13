package com.frobro.bcindex.api.controller;

import com.frobro.bcindex.api.IndexApiApp;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

  @RequestMapping("/")
  public String hone() {
    return IndexApiApp.class.getName() + " is up";
  }

  @RequestMapping("/status")
  public String healthCheck() {
    return "app is up";
  }
}
