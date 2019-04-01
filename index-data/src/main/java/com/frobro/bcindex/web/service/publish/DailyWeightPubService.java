package com.frobro.bcindex.web.service.publish;

import com.frobro.bcindex.core.model.WeightApi;
import com.frobro.bcindex.core.model.WeightDto;
import com.frobro.bcindex.core.service.BletchDate;
import com.frobro.bcindex.web.bclog.BcLog;

import java.time.Clock;

public class DailyWeightPubService extends PublishService {
  private static final BcLog LOG = BcLog.getLogger(DailyWeightPubService.class);
  private long publishCount = 0;
  private Clock clock = Clock.systemUTC();
  private long publishTimeMillis;

  // WARNING
  // possible data loss if app dies after 0 UTC
  // but before a publish happens. 
  // TODO: persist the time somewhere and read it on startup to fix
  public DailyWeightPubService() {
    resetPublishTime();
  }

  public DailyWeightPubService(Clock clock) {
    this.clock = clock;
    resetPublishTime();
  }

  @Override
  public String publishEndPtKey() {
    return "dailyWeightEndPoint";
  }

  public void publish(WeightApi data) {
    if (publishTimeMillis - data.getTime() <= 0) {
      publish(data.getWeightDto());
      resetPublishTime();
      publishCount++;
      LOG.info("Published daily weight data");
    }
  }

  protected void publish(WeightDto dto) {
    httpPublisher.publish(dto);
  }

  public long getNumTimesPublished() {
    return publishCount;
  }

  private void resetPublishTime() {
    publishTimeMillis = BletchDate.getZeroUtcTomorrow(clock);
  }
}
