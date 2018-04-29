package com.frobro.bcindex.api.controller;

import com.frobro.bcindex.api.service.WeightService;
import com.frobro.bcindex.api.service.weight.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeightController {

  @Autowired
  public void initWeightRepo(WeightTenRepo ten,
                             WeightTwentyRepo twenty,
                             WeightFortyRepo forty,
                             WeightTotalRepo total,
                             WeightEthRepo eth,
                             WeightCurrencyRepo curr,
                             WeightPlatformRepo plat,
                             WeightAppRepo app) {

    WeightService weightService = new WeightService(
        ten,twenty,forty,total,eth,curr,plat,app);
  }

  @RequestMapping("/data/weight")
  public String dataWeight() {
    return "accept weights";
  }
}
