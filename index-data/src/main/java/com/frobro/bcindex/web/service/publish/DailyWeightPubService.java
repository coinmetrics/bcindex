package com.frobro.bcindex.web.service.publish;

import com.frobro.bcindex.core.db.model.WeightApi;
import com.frobro.bcindex.core.db.service.BletchDate;
import com.frobro.bcindex.web.bclog.BcLog;

import java.time.Clock;

public class DailyWeightPubService extends PublishService {
  private static final BcLog LOG = BcLog.getLogger(DailyWeightPubService.class);
  private long publishCount = 0;
  private Clock clock = Clock.systemUTC();
  private long publishTimeMillis;

  // possible data loss if app dies after 0 UTC
  // but before a publish happens. In this rare event
  // can just add the missing data point to the db
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

  @Override
  public void publish(Object data) {
//    if (publishTimeMillis - data.getTime() <= 0) {
      super.publish(data);
      resetPublishTime();
      publishCount++;
      LOG.info("Publishing daily weight data");
//    }
  }

  public long getNumTimesPublished() {
    return publishCount;
  }

  private void resetPublishTime() {
    publishTimeMillis = BletchDate.getZeroUtcTomorrow(clock);
  }
}
