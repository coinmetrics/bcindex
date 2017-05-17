package com.frobro.bcindex.web.controller;

import com.frobro.bcindex.web.domain.Index;
import com.frobro.bcindex.web.service.TickerService;
import com.frobro.bcindex.web.service.TimerService;
import com.frobro.bcindex.web.service.persistence.EvenIdxRepo;
import com.frobro.bcindex.web.service.persistence.IndexRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
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
  private TimerService timerService;

  @Autowired
  public void init(IndexRepo repo, EvenIdxRepo eRepo) {
    tickerService.setIndexRepo(repo, eRepo);
    timerService = new TimerService(tickerService);
    timerService.run();
  }

  @PostConstruct
  public void init(){
    timerService.updateIfInDevMode();
  }

}
