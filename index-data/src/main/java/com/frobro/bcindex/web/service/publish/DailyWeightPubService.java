package com.frobro.bcindex.web.service.publish;

import com.frobro.bcindex.core.model.WeightApi;
import com.frobro.bcindex.core.model.WeightDto;
import com.frobro.bcindex.core.service.BletchDate;
import com.frobro.bcindex.web.bclog.BcLog;

import java.time.Clock;

public class DailyWeightPubService extends PublishService {
  private static final BcLog LOG = BcLog.getLogger(DailyWeightPubService.class);
  private final DailyTimer dailyTimer;

  // WARNING
  // possible data loss if app dies after 0 UTC
  // but before a publish happens. In this rare event
  // can just add the missing data point to the db
  public DailyWeightPubService() {
    this.dailyTimer = new DailyTimer();
  }

  public DailyWeightPubService(Clock clock) {
    this.dailyTimer = new DailyTimer(clock);
  }

  @Override
  public String publishEndPtKey() {
    return "dailyWeightEndPoint";
  }

  public void publish(WeightApi data) {
    if (dailyTimer.shouldFire(data.getTime())) {
      publish(data.getWeightDto());
      dailyTimer.fire();
      LOG.info("Published daily weight data");
    }
  }

  protected void publish(WeightDto dto) {
    httpPublisher.publish(dto);
  }

  public long getNumTimesPublished() {
    return dailyTimer.getTimesFired();
  }
}
