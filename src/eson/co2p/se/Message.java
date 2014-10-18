package eson.co2p.se;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by isidor on 2014-10-15.
 *
 *  Includes all messages sent from the client to the nameserver or chatserver
 */
public class Message {

    /**
     * creates a message that's supposed to be sent to the nameserver
     * asking for a list of servers
     *
     * @return the created pdu with headers
     */
    public static byte[] getServerMessage(){
        PDU rawdata = new PDU(4);
        rawdata.setByte(0,(byte)OpCodes.GETLIST);
        return rawdata.getBytes();
    }

    /**
     * creates a message to be sent to a server asking for connection
     *
     * @return  the created pdu with headers.
     */
    public static byte[] connectToServerMessage(){
        int usernameLength = catalogue.getNick().getBytes().length;
        PDU rawdata = new PDU(4 + div4(usernameLength));
        rawdata.setByte(0,(byte)OpCodes.JOIN);
        rawdata.setByte(1, (byte) usernameLength);
        try {
            rawdata.setSubrange(4, catalogue.getNick().getBytes("UTF-8"));
        }catch(UnsupportedEncodingException e){
            System.out.println("Error encoding nickname: " + e);
            e.printStackTrace();
        }
        return rawdata.getBytes();
    }

    /**
     * creates a pdu for sending a quit-message to the server
     *
     * @return the created PDU with headers.
     */
    public static byte[] quitServer(){
        PDU rawdata = new PDU(4);
        rawdata.setByte(0,(byte)OpCodes.QUIT);
        return rawdata.getBytes();
    }

    /**
     * changes the nick of the user
     *
     * @param nickname the name to be changed to
     * @return the created PDU with headers and nick
     */
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

    /**
     *  String message = the message to be sent
     *  int type = type of the message, eg. 0 = ordinary text, 1=compressed message
     *  2=crypt message 3=compressed and crypt message
     *
     *  @param message the message to be sent
     *  @param type ordinary/compressed/crypt
     *  @return the converted bytearray containing the PDU header + message
     */
    public static byte[] sendMessage(String message, int type){
        PDU rawdata = new PDU(12 + div4(message.getBytes().length +
                catalogue.getNick().getBytes().length));
        rawdata.setByte(0, (byte) OpCodes.MESSAGE);
        rawdata.setByte(1,(byte)type);
        rawdata.setByte(2, (byte)catalogue.getNick().getBytes().length);
        rawdata.setByte(3, (byte)0);
        rawdata.setShort(4, (short) message.getBytes().length);
        rawdata.setInt(8, getTime());
        try {
            rawdata.setSubrange(12, message.getBytes("UTF-8"));
            rawdata.setSubrange(12 + message.getBytes().length, catalogue.getNick().getBytes("UTF-8"));
        }catch(UnsupportedEncodingException e){
            System.out.println("Unsupported Encoding Exception: " + e);
        }
        //After all the bytes has been inserted and the checksum = 0,
        //calculate the real checksum
        rawdata.setByte(3, Checksum.calc(rawdata.getBytes(), rawdata.length()));
        return rawdata.getBytes();
    }

    /**
     * div4 tests if and int is divisible by four, if it isn't return the
     * rounded up number to ciel that's divisible by four.
     *
     * @param testInt the int to be tested if it is modulus 4
     * @return the int that's tested plus ciel modulus 4 of that int
     */
    public static int div4(int testInt){
        int ret = 0;
        if((4 -(testInt % 4)) != 0){
            ret = (4 -(testInt % 4));
        }
        return testInt + ret;
    }

    /**
     * getTime returns the time in seconds since the 1970's
     *
     * @return the time in seconds since the 1970's
     */
    public static int getTime(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2"));
        calendar.clear();
        calendar.set(2011, Calendar.OCTOBER, 1);
        return (int)(calendar.getTimeInMillis() / 1000L);
    }
}
