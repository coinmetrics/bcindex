package com.frobro.bcindex.web.controller;

import com.frobro.bcindex.core.db.service.EvenIdxRepo;
import com.frobro.bcindex.core.db.service.IndexRepo;
import com.frobro.bcindex.core.db.service.TwentyEvenRepo;
import com.frobro.bcindex.core.db.service.TwentyRepo;
import com.frobro.bcindex.web.service.TickerService;
import com.frobro.bcindex.web.service.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

/**
 * Created by rise on 3/21/17.
 */
@Controller
public class HomeController {

  private TickerService tickerService = new TickerService();
  private TimerService timerService;

  @Autowired
  public void init(IndexRepo repo, EvenIdxRepo eRepo,
                   TwentyRepo tRepo, TwentyEvenRepo teRepo) {
    tickerService.setIndexRepo(repo, eRepo, tRepo, teRepo);
    timerService = new TimerService(tickerService);
  }

  @PostConstruct
  public void start(){
    timerService.run();
  }
}
