package com.frobro.bcindex.core.db.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApplicationRepo extends CrudRepository<JpaApplication, Long> {
  List<JpaApplication> findByTimeStamp(long timeStamp);
  JpaApplication findFirstByOrderByIdDesc();
}