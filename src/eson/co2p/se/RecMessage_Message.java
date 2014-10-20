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

        type = PDUData.getByte(1);
        nickLength = PDUData.getByte(2);
        msgLength = PDUData.getShort(4);
        time = (int) PDUData.getInt(8);
        try {
            switch(type){
                case 0:
                    message = new String(PDUData.getSubrange(12, msgLength), "UTF-8");
                    break;
                case 1:
                    System.out.println("HITTADE KOMPRIMERAT MEDDELANDE");
                    break;
                case 2:
                    System.out.println("HITTADE KRYPTERAT FUCKING MEDDELANDE");
                    message = new String(deCrypt(PDUData.getSubrange(12, msgLength)), "UTF-8");
                    break;
                case 3:
                    System.out.println("HITTADE KOMPRIMERAD OCH KRYPTERAT MEDDELANDE");
                    message = new String(deCrypt(PDUData.getSubrange(12, msgLength)), "UTF-8");
                    break;
            }
            nickname = new String(PDUData.getSubrange((12 + msgLength), nickLength), "UTF-8");
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

    private static byte[] deCrypt(byte[] cryptMsg){
        PDU encryptedPDU = new PDU(cryptMsg, cryptMsg.length);
        int algorithm = encryptedPDU.getByte(0);
        int checksum = encryptedPDU.getByte(1);
        int cryptLength = encryptedPDU.getShort(2);
        int unCryptLength = encryptedPDU.getShort(4);
        byte[] encryptedMsg = encryptedPDU.getSubrange(12, cryptLength);
        Crypt.decrypt(encryptedMsg, encryptedMsg.length,catalogue.getKey(), catalogue.getKey().length);
        return encryptedMsg;
    }
}
