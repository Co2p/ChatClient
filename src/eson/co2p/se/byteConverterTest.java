package eson.co2p.se;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static eson.co2p.se.byteConverter.headerBuilder;

public class byteConverterTest extends TestCase {

    ArrayList<Integer> format = new ArrayList<Integer>();
    ArrayList<String> content = new ArrayList<String>();
    ByteArrayOutputStream output = new ByteArrayOutputStream();

    @Before
    public void setUp() throws Exception {
        super.setUp();
        format.add(8);
        format.add(16);
        content.add("1");
        content.add("AB");
    }

    @Test
    public void testHeaderBuilder() throws Exception {
        assertEquals(output, headerBuilder(format, content));
    }
}