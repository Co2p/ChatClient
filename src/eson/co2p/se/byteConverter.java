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

        //Här kommer lite ny kod
        for(int i = 0; i < totForm; i++){
            if(content.get(i).getClass().equals(int.class)){
                int intConv = (Integer)content.get(i);
                sendBytes[i] = (byte)intConv;
            }else{
                if(content.get(i) != null){
                    String stringConv = (String)content.get(i);
                }else{
                    sendBytes[i] = (byte)0;
                }
            }
            if(format.get(i) > 1){i += (format.get(i) - 1);}
            //Här ska även checkar om den skrivit ut alla nollor, detta har inte gjorts än.
        }

        return sendBytes;

        /*//här är gamla koden
        byte OP = (byte) OPCode;
        stringByte[0] = OP;
        for(int i = 1;i < format.size(); i++){
            try {
                if(content.get(i) != null) {
                    //stringByte[i] = content.get(i).getBytes("UTF-8");;
                    //If the added content is smaller than the given bytelength, add /0's until everything is filled
                    //for(int j = content.get(i).getBytes("UTF-8").length;
                    //    j < format.get(i); j++){
                    //    outputStream.write("\0".getBytes("UTF-8"));
                    //}
                }else{
                    for(int j = 0; j < format.get(i); j++){
                        stringByte[i] = (byte)0;
                        //outputStream.write("\0".getBytes("UTF-8"));
                    }
                }

            }catch(Exception e){
                System.out.println("Exception occurred during byte-conversion: " + e);
                e.printStackTrace();
            }
        }
        return outputStream;*/
    }
}
