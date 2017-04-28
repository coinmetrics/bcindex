package com.frobro.bcindex.web.controller;

import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.service.TickerService;
import com.frobro.bcindex.web.service.persistence.IndexRepo;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  public void setIndexRepo(IndexRepo repo) {
    tickerService.setIndexRepo(repo);
  }

  @RequestMapping("/")
  public String index(Model model) throws IOException {
    return renderHome(model);
  }

  @RequestMapping("/home")
  public String home(Model model) throws IOException {
    return renderHome(model);
  }

  private String renderHome(Model model) throws IOException {
    tickerService.updateTickers();

    Collection<Index> idxList = tickerService.getLatestCap();
    model.addAttribute("idxList", idxList);

    double latestSum = tickerService.getIndexValue();
    model.addAttribute("indexValue", format(latestSum));

    double latestEven = tickerService.getEvenIndexValue();
    model.addAttribute("evenIndex", format(latestEven));

    tickerService.saveIndex();

    return "home";
  }

  private String format(double val) {
    DecimalFormat df = new DecimalFormat("#.####");
    df.setRoundingMode(RoundingMode.CEILING);
    return df.format(val);
  }
}
