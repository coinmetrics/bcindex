package com.frobro.bcindex.core.db.service;


import com.frobro.bcindex.core.db.domain.JpaIdxTwenty;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by rise on 6/13/17.
 */
public interface TwentyRepo extends CrudRepository<JpaIdxTwenty, Long> {
  List<JpaIdxTwenty> findByTimeStamp(long timeStamp);
  JpaIdxTwenty findFirstByOrderByIdDesc();
}
