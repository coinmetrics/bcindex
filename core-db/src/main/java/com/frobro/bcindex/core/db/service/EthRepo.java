package com.frobro.bcindex.core.db.service;

import com.frobro.bcindex.core.db.domain.JpaIdxEth;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by rise on 7/16/17.
 */
public interface EthRepo extends CrudRepository<JpaIdxEth, Long> {
  JpaIdxEth findFirstByOrderByIdDesc();
}
