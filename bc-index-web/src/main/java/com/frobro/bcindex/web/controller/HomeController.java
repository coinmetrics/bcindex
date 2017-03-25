package com.frobro.bcindex.web.controller;

import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.service.TickerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by rise on 3/21/17.
 */
@Controller
public class HomeController {

  private TickerService tickerService = new TickerService();

  @RequestMapping("/")
  public String index(Model model) throws IOException {
    tickerService.updateTickers();

    Collection<Index> idxList = tickerService.getLatestCap();
    model.addAttribute("idxList", idxList);

    double latestSum = tickerService.getLatestSum();
    model.addAttribute("lastSum", latestSum);

    return "home";
  }
}
