package com.frobro.bcindex.core.db.service;

import com.frobro.bcindex.core.db.domain.JpaIndexTotal;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TotalRepo extends CrudRepository<JpaIndexTotal, Long> {
  List<JpaIndexTotal> findByTimeStamp(long timeStamp);
  JpaIndexTotal findFirstByOrderByIdDesc();
}