package eson.co2p.se;

import java.math.BigInteger;
import java.util.ArrayList;
import java.io.*;

/**
 * Created by gordon on 08/10/14.
 *
 * Handles the conversion to bytestreams, and adds padding
 */
public class byteConverter {

    /**
     * Takes two Arraylists, one, "format" containing the length of each element of the header
     * and the other "content" contains the data to be added into the stream of bytes and
     * converts both to UTF-8 and then creates a bytestream with the given size-parameters
     * for each element.
     *
     * @param   format Arraylist containing the size of each element to be used
     * @param   content Arraylist containing the data to use
     * @param   totLength the total length of the stream to send
     * @return  The created PDU
     */
    public static PDU headerBuilder(ArrayList<Integer> format, ArrayList<Object> content, int totLength){
        PDU pdu = new PDU(totLength);
        //As the first element of the array always will be the opcode, add this first.
        int tempInt = (Integer) content.get(0);
        pdu.setByte(0 ,(byte)tempInt);

        for(int i = 1; i < format.size(); i++){
            //Check if the content equals integer as the PDU handles integers and strings differently
            if(content.get(i) != null) {
                if (content.get(i) instanceof Integer) {
                    tempInt = (Integer) content.get(i);
                    pdu.setInt(format.get(i), tempInt);
                } else {
                    String tempString = (String) content.get(i);
                    pdu.setSubrange(format.get(i), tempString.getBytes());
                }
            }
        }
        return pdu;
    }
}
