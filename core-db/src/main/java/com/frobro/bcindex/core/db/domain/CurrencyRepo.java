package com.frobro.bcindex.core.db.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CurrencyRepo extends CrudRepository<JpaCurrency, Long> {
  List<JpaCurrency> findByTimeStamp(long timeStamp);
  JpaCurrency findFirstByOrderByIdDesc();
}