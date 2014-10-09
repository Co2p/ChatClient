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
    public static ByteArrayOutputStream headerBuilder(ArrayList<Integer> format, ArrayList<String> content, int OPCode){
        // Go trough the format-Array which contains the length of each element in the
        // array "content" The string is expected to be in UTF-8.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        byte[] stringByte = new byte[format.size() + 1];
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
        return outputStream;
    }
}
