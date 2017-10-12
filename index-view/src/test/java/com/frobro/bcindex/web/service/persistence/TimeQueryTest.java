package com.frobro.bcindex.web.service.persistence;

import com.frobro.bcindex.core.db.domain.JpaIndexTen;
import com.frobro.bcindex.web.service.query.CsvTimeQuery;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TimeQueryTest extends DbBaseTest {

  private static final long numDataPts = TimeUnit.DAYS.toMinutes(1);
  private static final long INTERVAL = TimeUnit.MINUTES.toMillis(1);
  private static final long START_TIME = System.currentTimeMillis();
  private static List<JpaIndexTen> data = new ArrayList<>((int)numDataPts);

  @BeforeClass
  public static void setup() {
    long time = START_TIME;
    int i;
    for (i=0; i<numDataPts; i++) {
      newDataPt(time, i);
      time += INTERVAL;
    }
    newDataPt(time,i);
  }

  private static void newDataPt(long time, int i) {
    JpaIndexTen idx = new JpaIndexTen();
    idx.setTimeStamp(time);
    idx.setIndexValueBtc(1.0 * i);
    idx.setIndexValueUsd(10.0*i);
    data.add(idx);
  }

  @Test
  public void test() {
    // given
    populateDb();
    CsvTimeQuery csv = new CsvTimeQuery(jdbc, day());

    // when
    List<String> lines = csv.getCsvContent("ODD_INDEX");

    // then expect 2 data points + 1 line header = 3 lines
    assertEquals(3, lines.size());
  }

  private void populateDb() {
    this.oddRepo.save(data);
  }

  private long day() {
    return TimeUnit.DAYS.toMillis(1);
  }
}
