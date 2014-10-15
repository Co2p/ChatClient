package eson.co2p.se;

/**
 * Created by Isidor Nygren on 2014-10-15.
 */
public class connectToServerMessage extends message {

    public connectToServerMessage(String username){
        int usernameLength = username.getBytes().length;
        this.addOp(OpCodes.JOIN);
        this.getRawdata().extendTo(4 + div4(usernameLength));
        this.getRawdata().setByte(1, (byte)usernameLength);
        this.getRawdata().setSubrange(4, username.getBytes());
    }
}
