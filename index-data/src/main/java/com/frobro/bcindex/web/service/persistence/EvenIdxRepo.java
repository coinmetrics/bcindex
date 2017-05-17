package com.frobro.bcindex.web.service.persistence;

import com.frobro.bcindex.web.domain.JpaEvenIndex;
import com.frobro.bcindex.web.domain.JpaIndex;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by rise on 5/10/17.
 */
public interface EvenIdxRepo extends CrudRepository<JpaEvenIndex, Long> {
  List<JpaEvenIndex> findByTimeStamp(Date timeStamp);
}

