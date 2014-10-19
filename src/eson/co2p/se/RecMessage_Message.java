package eson.co2p.se;

import java.io.UnsupportedEncodingException;

/**
 * Created by Isidor on 2014-10-18.
 */
public class RecMessage_Message extends RecMessage{
    int type, time;
    String nickname, message;

    public RecMessage_Message(byte[] rawData){
        super(rawData);
        int nickLength, msgLength;
        int checksum = PDUData.getByte(3);
        System.out.println("CHECKSUM: " + checksum);
        //Test the checksum first and print out error if not equal.
        int calcChecksum = Checksum.calc(PDUData.getBytes(), PDUData.length());
        if(calcChecksum != checksum){
            System.out.println("Checksum test failed:" + calcChecksum + " != " + checksum);
        }

        System.out.println("TOTLENGTH: " + rawData.length);
        type = PDUData.getByte(1);
        nickLength = PDUData.getByte(2);
        msgLength = PDUData.getShort(4);
        time = (int) PDUData.getInt(8);
        try {
            System.out.println("msgLength : " + msgLength + ", nickLength: " + nickLength);
            message = new String(PDUData.getSubrange(12, msgLength), "UTF-8");
            nickname = new String(PDUData.getSubrange(Message.div4(12 + msgLength), nickLength), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error encoding incoming message: " + e);
            e.printStackTrace();
        }
    }

    public String getMessage(){
        return message;
    }

    public String getNick(){
        return nickname;
    }

    public int getTime(){
        return time;
    }
}
