package eson.co2p.se;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by isidor on 2014-10-15.
 *
 *  Handles the abstract creation of messages, includes all methods shared by
 *  all messageclasses.
 */
public class message {

    public static byte[] getServerMessage(){
        PDU rawdata = new PDU(4);
        rawdata.setByte(0,(byte)OpCodes.GETLIST);
        return rawdata.getBytes();
    }

    public static byte[] connectToServerMessage(String username){
        int usernameLength = username.getBytes().length;
        PDU rawdata = new PDU(4 + div4(usernameLength));
        rawdata.setByte(0,(byte)OpCodes.JOIN);
        rawdata.setByte(1, (byte) usernameLength);
        rawdata.setSubrange(4, username.getBytes());
        return rawdata.getBytes();
    }

    public static byte[] quitServer(){
        PDU rawdata = new PDU(4);
        rawdata.setByte(0,(byte)OpCodes.QUIT);
        return rawdata.getBytes();
    }

    public static byte[] changeNick(String nickname){
        PDU rawdata = new PDU(4 + div4(nickname.getBytes().length));
        catalogue.setName(nickname);
        rawdata.setByte(0,(byte)OpCodes.CHNICK);
        rawdata.setByte(1, (byte) nickname.getBytes().length);
        try {
            rawdata.setSubrange(4, nickname.getBytes("UTF-8"));
        }catch(UnsupportedEncodingException e){
            System.out.println("Unsupported Encoding Exception: " + e);
        }
        return rawdata.getBytes();
    }

    /*
     *
     *  String message = the message to be sent
     *  int type = type of the message, eg. 0 = ordinary text, 1=compressed message
     *  2=crypt message 3=compressed and crypt message
     */
    public static byte[] sendMessage(String message, int type, int checksum){
        PDU rawdata = new PDU(12 + div4(message.getBytes().length +
                catalogue.getNick().getBytes().length));
        rawdata.setByte(0, (byte) OpCodes.MESSAGE);
        rawdata.setByte(1,(byte)type);
        rawdata.setByte(2, (byte)catalogue.getNick().getBytes().length);
        rawdata.setByte(3, (byte)checksum);
        rawdata.setShort(4, (short) message.getBytes().length);
        rawdata.setInt(8, getTime());
        try {
            rawdata.setSubrange(12, message.getBytes("UTF-8"));
            rawdata.setSubrange(12 + message.getBytes().length, catalogue.getNick().getBytes("UTF-8"));
        }catch(UnsupportedEncodingException e){
            System.out.println("Unsupported Encoding Exception: " + e);
        }
        return rawdata.getBytes();
    }

    public static int div4(int testInt){
        int ret = 0;
        if((4 -(testInt % 4)) != 0){
            ret = (4 -(testInt % 4));
        }
        return testInt + ret;
    }

    public static int getTime(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2"));
        calendar.clear();
        calendar.set(2011, Calendar.OCTOBER, 1);
        return (int)(calendar.getTimeInMillis() / 1000L);
    }
}
