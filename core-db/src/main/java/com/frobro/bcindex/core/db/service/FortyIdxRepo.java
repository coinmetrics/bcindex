package com.frobro.bcindex.core.db.service;

import com.frobro.bcindex.core.db.domain.JpaIdxForty;
import org.springframework.data.repository.CrudRepository;

public interface FortyIdxRepo extends CrudRepository<JpaIdxForty, Long> {
  JpaIdxForty findFirstByOrderByIdDesc();
}
