package com.frobro.bcindex.core.db.service;

import com.frobro.bcindex.core.db.domain.JpaEvenIndex;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by rise on 5/10/17.
 */
public interface EvenIdxRepo extends CrudRepository<JpaEvenIndex, Long> {
  List<JpaEvenIndex> findByTimeStamp(long timeStamp);
  JpaEvenIndex findFirstByOrderByIdDesc();
}

