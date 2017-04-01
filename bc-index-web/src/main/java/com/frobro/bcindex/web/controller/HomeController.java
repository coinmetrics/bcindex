package com.frobro.bcindex.web.controller;

import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.service.TickerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collection;

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

    double latestSum = tickerService.getIndexValue();
    DecimalFormat df = new DecimalFormat("#.####");
    df.setRoundingMode(RoundingMode.CEILING);
    model.addAttribute("indexValue", df.format(latestSum));

    return "home";
  }
}
