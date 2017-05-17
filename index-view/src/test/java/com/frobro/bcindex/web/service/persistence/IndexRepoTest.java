package com.frobro.bcindex.web.service.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.frobro.bcindex.web.domain.JpaIndex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * Created by rise on 4/19/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexRepoTest {

  private IndexRepo indexRepo;

  @Autowired
  public void setIndexRepo(IndexRepo repo) {
    this.indexRepo = repo;
  }

  @Test
  public void testSave() {
    JpaIndex index = JpaIndex.create()
        .setTimeStamp(new Date())
        .setIndexValueBtc(12)
        .setIndexValueUsd(34)
        .setId(1234567L);

    indexRepo.save(index);
    JpaIndex retreived = indexRepo.findByTimeStamp(index.getTimeStamp()).get(0);

    assertEquals(index.getIndexValueBtc(), retreived.getIndexValueBtc(), 0.01);
    assertEquals(index.getIndexValueUsd(), retreived.getIndexValueUsd(), 0.01);
  }

  @Test
  public void testGetLatest() {
    // given
    JpaIndex first = IndexFactory.getNewOdd().setId(1L);
    // and
    sleep(1);
    // and
    JpaIndex second = IndexFactory.getNewOdd().setId(2L);
    // and
    indexRepo.save(first);
    indexRepo.save(second);

    // when
    JpaIndex latest = indexRepo.findFirst1ByOrderByTimeStampDesc().get(0);

    // then
    assertEquals(second.getId(), latest.getId());
    assertEquals(second.getIndexValueBtc(), latest.getIndexValueBtc(), 0.001);
    assertEquals(second.getIndexValueUsd(), latest.getIndexValueUsd(), 0.001);
    assertEquals(second.getTimeStamp().getTime(), latest.getTimeStamp().getTime());
    assertNotEquals(first.getIndexValueUsd(), latest.getIndexValueUsd());
  }

  private void sleep(long millis) {
    try { Thread.sleep(millis); } catch (InterruptedException ie) {
      throw new RuntimeException(ie);
    }
  }
}
