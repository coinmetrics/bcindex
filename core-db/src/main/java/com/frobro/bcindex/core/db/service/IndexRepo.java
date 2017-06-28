package com.frobro.bcindex.core.db.service;

import com.frobro.bcindex.core.db.domain.JpaIndexTen;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by rise on 4/19/17.
 */

public interface IndexRepo extends CrudRepository<JpaIndexTen, Long> {
  List<JpaIndexTen> findByTimeStamp(long timeStamp);
  JpaIndexTen findFirstByOrderByIdDesc();
}
