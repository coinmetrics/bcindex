package com.frobro.bcindex.web.service.persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DailyTimerRepo extends CrudRepository<DailyFireTimes, Long> {

  @Query(value = "select * from DAILY_FIRE_TIMES where TIMER_NAME = ?1 order by ACTUAL_FIRE_TIME desc limit 1", nativeQuery = true)
  List<DailyFireTimes> getLastFireTime(String name);
}
