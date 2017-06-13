package com.frobro.bcindex.core.db.service;


import com.frobro.bcindex.core.db.domain.JpaTwentyEven;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by rise on 6/13/17.
 */
public interface TwentyEvenRepo extends CrudRepository<JpaTwentyEven, Long> {
  List<JpaTwentyEven> findByTimeStamp(Date timeStamp);
}
