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
        format.add(1);
        format.add(3);
        format.add(1);
        content.add("1");
        content.add(null);
        content.add("AB");
        for(int i = 0; i < content.size(); i++){
            if(content.get(i) != null) {
                output.write(content.get(i).getBytes("UTF-8"));
            }else{
                for(int j = 0; j < format.get(i); j++ ) {
                    output.write("\0".getBytes("UTF-8"));
                }
            }
        }
    }

    @Test
    public void testHeaderBuilder() throws Exception {
        //assertEquals funkar enbart om båda objekten som testas är EXAKT samma objekt.
        assertEquals(output, headerBuilder(format, content));
    }
}