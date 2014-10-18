package eson.co2p.se;

import java.io.UnsupportedEncodingException;

/**
 * Created by Isidor on 2014-10-18.
 */
public class RecMessage_Message extends RecMessage{
    int type, checksum, time;
    String nickname, message;

    public RecMessage_Message(byte[] rawData){
        super(rawData);
        int nickLength, msgLength;

        //Test the checksum first and print out error if not equal.
        if(Checksum.calc(rawData, rawData.length) != 0xff){
            System.out.println("Checksum test failed");
        }

        type = PDUData.getByte(1);
        nickLength = PDUData.getByte(2);
        checksum = PDUData.getByte(3);
        msgLength = PDUData.getShort(4);
        time = (int)PDUData.getInt(8);
        try {
            message = new String(PDUData.getSubrange(12, msgLength), "UTF-8");
            nickname = new String(PDUData.getSubrange(12+msgLength, nickLength), "UTF-8");
        }catch(UnsupportedEncodingException e){
            System.out.println("Error encoding incoming message: " + e);
            e.printStackTrace();
        }
    }
}
