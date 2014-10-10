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
     * @return  the fully converted ByteArrauOutputStream
     */
    public static byte[] headerBuilder(ArrayList<Integer> format, ArrayList<Object> content){
        // Go trough the format-Array which contains the length of each element in the
        // array "content" The string is expected to be in UTF-8.
        int totForm = 0;
        for(int i = 0; i < format.size(); i++){
            totForm += format.get(i);
        }
        byte[] sendBytes = new byte[totForm];
        for(int i = 0; i < totForm; i++){
            byte[] tempBytes;
            if(content.get(i) instanceof Integer){
                int intConv = (Integer)content.get(i);
                sendBytes[i] = (byte)intConv;
                //If the element for the int is larger than one byte, add as many bytes as given in the format
                for(int j = 1; j < format.get(i); j++){
                    sendBytes[i+j] = (byte)0;
                }
            }else{
                if(content.get(i) != null){
                    String stringConv = (String)content.get(i);
                    tempBytes = stringConv.getBytes();
                    for(int j = 0; j < format.get(i); j++){
                        if(j < tempBytes.length) {
                            sendBytes[i + j] = tempBytes[j];
                        }else{
                            sendBytes[i + j] = (byte)0;
                        }
                    }
                }else{
                    //If nothing is given in the content element, just send zerobytes
                    for(int j = 0; j < format.get(i); j++) {
                        sendBytes[i + j] = (byte) 0;
                    }
                }
            }
        }
        return sendBytes;
    }
}
