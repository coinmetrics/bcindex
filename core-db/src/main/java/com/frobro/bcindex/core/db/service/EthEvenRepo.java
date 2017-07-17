package com.frobro.bcindex.core.db.service;

import com.frobro.bcindex.core.db.domain.JpaIdxEthEven;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by rise on 7/16/17.
 */
public interface EthEvenRepo extends CrudRepository<JpaIdxEthEven, Long> {
  JpaIdxEthEven findFirstByOrderByIdDesc();
}
