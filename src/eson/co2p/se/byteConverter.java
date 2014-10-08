package eson.co2p.se;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by gordon on 08/10/14.
 *
 * Handles the conversion to bytestreams, and adds padding
 */
public class byteConverter {

    Byte zeroByte;

    public void headerBuilder(ArrayList<Integer> format, ArrayList<String> content){
        // Go trough the format-Array which contains the length of each element in the
        // array "content" The string is expected to be in UTF-8.
        for(int i = 0;i < format.size(); i++){
            //TODO  skapar en enkel sträng här bara för test, denna ska egentligen vara
            //      en ström av bytes
            String test;

        }
    }
}
