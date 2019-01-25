package com.frobro.bcindex.web.controller;

import com.frobro.bcindex.core.db.domain.ApplicationRepo;
import com.frobro.bcindex.core.db.domain.CurrencyRepo;
import com.frobro.bcindex.core.db.domain.PlatformRepo;
import com.frobro.bcindex.core.db.service.*;
import com.frobro.bcindex.web.service.DailyTimer;
import com.frobro.bcindex.web.service.TickerService;
import com.frobro.bcindex.web.service.TimerService;
import com.frobro.bcindex.web.service.persistence.DailyTimerRepo;
import com.frobro.bcindex.web.service.publish.DailyWeightPubService;
import com.frobro.bcindex.web.service.publish.PricePublishService;
import com.frobro.bcindex.web.service.publish.WeightPublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by rise on 3/21/17.
 */
@RestController
public class HomeController {

  private TickerService tickerService = new TickerService();
  private TimerService timerService;
  private WeightPublishService weightPublisher;
  private PricePublishService pricePublisher;
  private DailyWeightPubService dailyWeightPub;

  @Autowired
  public void init(Environment env, IndexRepo repo, EvenIdxRepo eRepo,
                   TwentyRepo tRepo, TwentyEvenRepo teRepo,
                   EthRepo etRepo, EthEvenRepo eteRepo,
                   FortyIdxRepo fRepo, FortyEvenIdxRepo feRepo,
                   TotalRepo toRepo, TotalEvenRepo toeRepo,
                   CurrencyRepo cRepo, PlatformRepo pRepo,
                   ApplicationRepo aRepo, DailyTimerRepo dtr) {

    DailyTimer.setRepo(dtr);

    weightPublisher = new WeightPublishService();
    pricePublisher = new PricePublishService();
    dailyWeightPub = new DailyWeightPubService();

    // pull values from application.properties
    weightPublisher.createPublishEndPoint(env.getProperty(weightPublisher.publishEndPtKey()));
    pricePublisher.createPublishEndPoint(env.getProperty(pricePublisher.publishEndPtKey()));
    dailyWeightPub.createPublishEndPoint(env.getProperty(dailyWeightPub.publishEndPtKey()));

    tickerService.setIndexRepo(repo, eRepo,
                               tRepo, teRepo,
                               etRepo, eteRepo,
                               fRepo, feRepo,
                               toRepo, toeRepo,
                               cRepo, pRepo, aRepo);

    tickerService.setWeightPublisher(weightPublisher);
    tickerService.setDailyPxPublisher(pricePublisher);
    tickerService.setDailyWeightPublisher(dailyWeightPub);

    timerService = new TimerService(tickerService);
  }

  @RequestMapping("/bc-health-check")
  public String healthCheck() {
    return "app is up";
  }

  @PostConstruct
  public void start(){
    timerService.run();
  }
}
