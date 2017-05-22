package com.frobro.bcindex.web.service.persistence;

import com.frobro.bcindex.web.domain.JpaIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by rise on 4/19/17.
 */

public interface IndexRepo extends CrudRepository<JpaIndex, Long> {
  List<JpaIndex> findByTimeStamp(Date timeStamp);
  List<JpaIndex> findFirst1ByOrderByTimeStampDesc();
}
