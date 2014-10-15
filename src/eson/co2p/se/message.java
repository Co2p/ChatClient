package eson.co2p.se;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by isidor on 2014-10-15.
 *
 *  Handles the abstract creation of messages, includes all methods shared by
 *  all messageclasses.
 */
public class message {
    //op is used for testing purposes and is not supposed to be used by
    //the program itself
    private int op;
    private PDU rawdata;

    public byte[] getServerMessage(){
        addOp(OpCodes.GETLIST);
        getRawdata().extendTo(4);
        return getData();
    }

    public byte[] connectToServerMessage(String username){
        addOp(OpCodes.JOIN);
        int usernameLength = username.getBytes().length;
        rawdata.extendTo(4 + div4(usernameLength));
        rawdata.setByte(1, (byte) usernameLength);
        rawdata.setSubrange(4, username.getBytes());
        return getData();
    }

    /*
     *
     *  String message = the message to be sent
     *  int type = type of the message, eg. 0 = ordinary text, 1=compressed message
     *  2=crypt message 3=compressed and crypt message
     */
    public byte[] sendMessage(String message, int type, int checksum){
        addOp(OpCodes.MESSAGE);
        rawdata.extendTo(12);
        rawdata.setByte(1,(byte)type);
        rawdata.setByte(2, (byte)catalogue.getNick().getBytes().length);
        rawdata.setByte(3, (byte)checksum);
        rawdata.setShort(4, (short) message.getBytes().length);
        rawdata.setInt(8, getTime());
    }

    void addOp(int op){
        rawdata = new PDU(1);
        this.op = op;
        rawdata.setByte(0, (byte)op);
    }

    public byte[] getData(){
        return rawdata.getBytes();
    }

    public PDU getRawdata(){
        return rawdata;
    }

    public int getOp(){
        return op;
    }
    public static int div4(int testInt){
        int ret = 0;
        if((4 -(testInt % 4)) != 0){
            ret = (4 -(testInt % 4));
        }
        return testInt + ret;
    }
    public int getTime(){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+2"));
        calendar.clear();
        calendar.set(2011, Calendar.OCTOBER, 1);
        return (int)(calendar.getTimeInMillis() / 1000L);
    }
}
