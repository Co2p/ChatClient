package eson.co2p.se;

import java.io.UnsupportedEncodingException;
import java.util.zip.*;
import java.io.*;

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
                    //Got a problem with UTF-8 and linux printing out the zerobytes
                    //This is added as a fix for that problem
                    message = message.replaceAll(new String(new byte[]{0}, "UTF-8"), "");
                    System.out.println("MESSAGE: " + message);
                    break;
                case 1:
                    System.out.println("HITTADE KOMPRIMERAT MEDDELANDE");
                    message = new String(deCompress(PDUData.getSubrange(12, msgLength)), "UTF-8");
                    break;
                case 2:
                    System.out.println("HITTADE KRYPTERAT FUCKING MEDDELANDE");
                    message = new String(deCrypt(PDUData.getSubrange(12, msgLength)), "UTF-8");
                    break;
                case 3:
                    System.out.println("HITTADE KOMPRIMERAD OCH KRYPTERAT MEDDELANDE");
                    message = new String(deCrypt(deCompress(PDUData.getSubrange(12, msgLength))), "UTF-8");
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

    public int getType(){
        return type;
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
    private static byte[] deCompress(byte[] comprMsg){
        PDU compressedPDU = new PDU(comprMsg, comprMsg.length);
        int algorithm = compressedPDU.getByte(0);
        int checksum = compressedPDU.getByte(1);
        int compLength = compressedPDU.getShort(2);
        int unCompLength = compressedPDU.getShort(4);
        byte[] compMsg = compressedPDU.getSubrange(8, compLength);
        byte[] retArr = null;
        if(algorithm == 0){
            try {
                Inflater inf = new java.util.zip.Inflater();
                ByteArrayInputStream bytein = new java.io.ByteArrayInputStream(compMsg);
                GZIPInputStream gzin = new java.util.zip.GZIPInputStream(bytein);
                ByteArrayOutputStream byteout = new java.io.ByteArrayOutputStream();
                int res = 0;
                byte buf[] = new byte[65000];
                while (res >= 0) {
                    res = gzin.read(buf, 0, buf.length);
                    if (res > 0) {
                        byteout.write(buf, 0, res);
                    }
                }
                retArr = byteout.toByteArray();
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            System.out.println("unknown compression method");
        }
        return retArr;
    }
}
