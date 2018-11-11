package com.frobro.bcindex.web.service.persistence;

import com.frobro.bcindex.web.model.api.IndexType;
import com.frobro.bcindex.web.service.TimeSeriesService;
import com.frobro.bcindex.web.service.query.GroupUpdate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupUpdateTest extends DbBaseTest {
    
    @Test
    public void testGroupUpdate() {
        // given data is populated
        double usdPx = 50.0;
        populateDb(100, usdPx);
        // and
        TimeSeriesService service = new TimeSeriesService();
        service.setJdbc(jdbc);

        // when data is the latest data is retrieved from the db
        GroupUpdate update = service.getLastestData();

        // then - expect each index to have the correct price
        for (IndexType index : IndexType.values()) {
            assertEquals(usdPx, update.get(index).getUsdPrice(), 0.001);
        }
    }
}
