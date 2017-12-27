package com.frobro.bcindex.core.db.service;

import com.frobro.bcindex.core.db.domain.JpaIndexTotalEven;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TotalEvenRepo extends CrudRepository<JpaIndexTotalEven, Long> {
  List<JpaIndexTotalEven> findByTimeStamp(long timeStamp);
  JpaIndexTotalEven findFirstByOrderByIdDesc();
}
