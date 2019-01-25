package com.frobro.bcindex.core.db.service;

import com.frobro.bcindex.core.db.domain.JpaIdxEth;
import org.springframework.data.repository.CrudRepository;

public interface MaxTimeRepo extends CrudRepository<JpaIdxEth, Long> {
  JpaIdxEth findFirstByOrderByIdDesc();
}
