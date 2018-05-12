package com.frobro.bcindex.web.testframework;

import com.frobro.bcindex.core.model.WeightDto;
import com.frobro.bcindex.web.service.publish.DailyWeightPubService;

import java.time.Clock;

/*
 * don't make any network calls while testing
 */
public class MockDailyWeightPubService extends DailyWeightPubService {

  public MockDailyWeightPubService(Clock clock) {
    super(clock);
  }

  @Override
  protected void publish(WeightDto dto) {
    // no op
  }
}
