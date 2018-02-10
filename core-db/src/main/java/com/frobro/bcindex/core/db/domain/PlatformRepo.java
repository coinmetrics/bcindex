package com.frobro.bcindex.core.db.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PlatformRepo extends CrudRepository<JpaPlatform, Long> {
  List<JpaPlatform> findByTimeStamp(long timeStamp);
  JpaPlatform findFirstByOrderByIdDesc();
}