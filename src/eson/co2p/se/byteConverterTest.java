package eson.co2p.se;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class byteConverterTest extends TestCase {

    ArrayList<Integer> format = new ArrayList<Integer>();
    ArrayList<String> content = new ArrayList<String>();
    ByteArrayOutputStream output = new ByteArrayOutputStream(24);

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
        assertEquals(output, headerBuilder());

    }
}