package com.frobro.bcindex.api.controller;

import com.frobro.bcindex.core.db.model.IndexPrice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DailyPriceController {

  /*
    @RequestMapping(value = "blet/weight", method = RequestMethod.POST)
  public void cacheUpdate(@RequestBody Map<String,Map<String,Double>> data) {

   */

  @RequestMapping("/data/price")
  public String dataWeight(@RequestBody Map<String,IndexPrice> data) {
    System.out.println("received data: " + data);
    return "accept prices";
  }
}
