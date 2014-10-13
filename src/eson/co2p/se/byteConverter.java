package eson.co2p.se;

import java.math.BigInteger;
import java.util.ArrayList;
import java.net.InetAddress;
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
        pdu.setByte(0, (byte) tempInt);

        for(int i = 1; i < format.size(); i++){
            //Check if the content equals integer as the PDU handles integers and strings differently
            if(content.get(i) != null) {
                if (content.get(i) instanceof Integer) {
                    tempInt = (Integer) content.get(i);
                    pdu.setByte(format.get(i), (byte) tempInt);
                } else {
                    String tempString = (String) content.get(i);
                    pdu.setSubrange(format.get(i), tempString.getBytes());
                }
            }
        }
        return pdu;
    }
    public static String NsClServerlist(byte[] message){
        PDU reMessage = new PDU(message, message.length);
        String returnString = "OP: ";
        int chatServers = (int) reMessage.getShort(2);
        int tot = 0;

        try {
            returnString += (int) reMessage.getByte(0) + " ";
            returnString += (int) reMessage.getByte(1) + " ";
            returnString += "antal chatservrar:" + chatServers + " ";
            tot = 4;
            returnString += "\n";
            for(int i = 0; i < chatServers; i++) {
                //returnString += new String(reMessage.getSubrange(tot, 4), "UTF-8");
                returnString += (InetAddress.getByAddress(reMessage.getSubrange(tot, 4)));
                tot += 4;
                returnString += " " + (int) reMessage.getShort(tot) + " ";
                tot += 2;
                returnString += (int) reMessage.getByte(tot) + " ";
                tot += 1;
                int serverNameLength = (int) reMessage.getByte(tot);
                returnString += "Length of serverName = " + serverNameLength + " ";
                tot += 1;
                returnString += new String(reMessage.getSubrange(tot, tot + serverNameLength), "UTF-8");
                tot += serverNameLength;
                returnString += "\n";
            }
        }catch(Exception e){
            System.out.println(e);
        }

        return returnString;
    }
}
