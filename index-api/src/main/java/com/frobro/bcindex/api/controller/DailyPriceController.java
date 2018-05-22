package com.frobro.bcindex.api.controller;

import com.frobro.bcindex.core.model.IndexName;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DailyPriceController {
  
  @RequestMapping(value = "/blet/daily/price", method = RequestMethod.POST)
  public String getDailyPrice(@RequestBody IndexName indexName) {
    return "implement me";
  }
}
