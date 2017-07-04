package com.frobro.bcindex.web;

import com.frobro.bcindex.web.model.api.RequestDto;
import com.frobro.bcindex.web.service.DoubleFormatter;
import org.junit.Ignore;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {
    
    @Test
    public void testApp() {
        RequestDto dto = new RequestDto();
        assertFalse(dto.reqValid());
        assertTrue(true);
    }
}
