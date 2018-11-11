package com.frobro.bcindex.core.db.service;

import com.frobro.bcindex.core.db.domain.JpaIdxFortyEven;
import org.springframework.data.repository.CrudRepository;

public interface FortyEvenIdxRepo extends CrudRepository<JpaIdxFortyEven, Long> {
  JpaIdxFortyEven findFirstByOrderByIdDesc();
}
