package eson.co2p.se;

import sun.jvm.hotspot.runtime.Bytes;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by gordon on 08/10/14.
 *
 * Handles the conversion to bytestreams, and adds padding
 */
public class byteConverter {

    Byte zeroByte;
    Bytes output;

    public void headerBuilder(ArrayList<Integer> format, ArrayList<String> content){
        
        // Go trough the format-Array which contains the length of each element in the
        // array "content"
        for (int i=0; format.size(0) >i; i++){
            //TODO  skapar en enkel sträng här bara för test, denna ska egentligen vara
            //      en ström av bytes

            for (int j=0; format.get(i)>j;j++){

            }
        }
    }

}
