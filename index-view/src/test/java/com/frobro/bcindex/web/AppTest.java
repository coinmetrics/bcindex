package com.frobro.bcindex.web;

import com.frobro.bcindex.web.model.api.RequestDto;
import com.frobro.bcindex.web.service.DoubleFormatter;
import org.junit.Ignore;
import org.junit.Test;

import java.text.DecimalFormat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Ignore
    @Test
    public void test() {
        double a = 4634.45352;
        double b = 0.349343;
        double c = 0.1;
        double d = 464553;

        System.out.println("a=" + DoubleFormatter.formatBtc(a));
        System.out.println("b=" + DoubleFormatter.formatBtc(b));
        System.out.println("c=" + DoubleFormatter.formatBtc(c));
        System.out.println("d=" + DoubleFormatter.formatBtc(d));
    }

    @Test
    public void testApp() {
        RequestDto dto = new RequestDto();
        assertFalse(dto.reqValid());
        assertTrue(true);
    }
}
