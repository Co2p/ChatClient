package eson.co2p.se;

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
        int usernameLength = username.getBytes().length;
        this.addOp(OpCodes.JOIN);
        this.getRawdata().extendTo(4 + div4(usernameLength));
        this.getRawdata().setByte(1, (byte)usernameLength);
        this.getRawdata().setSubrange(4, username.getBytes());
        return getData();
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
}
