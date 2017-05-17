package com.frobro.bcindex.web;

import com.frobro.bcindex.web.model.api.RequestDto;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void testApp() {
        RequestDto dto = new RequestDto();
        assertFalse(dto.isValid());
        assertTrue(true);
    }
}
