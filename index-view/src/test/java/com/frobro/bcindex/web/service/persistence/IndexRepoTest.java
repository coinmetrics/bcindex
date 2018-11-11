package com.frobro.bcindex.web.service.persistence;

import com.frobro.bcindex.core.db.domain.JpaIndex;
import com.frobro.bcindex.core.db.domain.JpaIndexTen;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by rise on 4/19/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexRepoTest extends DbBaseTest {

  @Test
  public void testSave() {
    JpaIndexTen index = new JpaIndexTen();
        index.setTimeStamp(new Date())
        .setIndexValueBtc(12)
        .setIndexValueUsd(34)
        .setId(1234567L);

    repo.saveTen(index);
    JpaIndexTen retreived = oddRepo.findByTimeStamp(index.getTimeStamp().getTime()).get(0);

    assertEquals(index.getIndexValueBtc(), retreived.getIndexValueBtc(), 0.01);
    assertEquals(index.getIndexValueUsd(), retreived.getIndexValueUsd(), 0.01);
  }

  @Test
  public void testGetLatest() {
    // given
    JpaIndexTen first = IndexFactory.getNewOdd();
    first.setId(1L);
    // and
    sleep(1);
    // and
    JpaIndexTen second = IndexFactory.getNewOdd();
    second.setId(2L);
    // and
    repo.saveTen(first);
    repo.saveTen(second);

    // when
    JpaIndex latest = oddRepo.findById(second.getId()).get();

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
