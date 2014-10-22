package eson.co2p.se;

import java.io.UnsupportedEncodingException;
import java.util.zip.*;
import java.io.*;

/**
 * Defines the OP messages sent to the nameserver and the server
 * @author Created by Isidor on 2014-10-18.
 */
public class RecMessage {
    private int op;
    PDU PDUData;
    int type, time,Tabid;
    int OriginType = 0;
    String nickname, message;

    public RecMessage(byte[] rawData, int Tabid){
        int nickLength;
        String nick = "";
        this.Tabid = Tabid;
        PDUData = new PDU(rawData, rawData.length);
        setOp(PDUData.getByte(0));

        switch(op){
            case OpCodes.MESSAGE:
                System.out.println("Found message!");
                Message();
                break;
            case OpCodes.NICKS:
                System.out.println("Found nicks!");
                OriginType = 1;
                try {
                    String nicknames = new String(PDUData.getSubrange
                            (4, Message.div4(PDUData.getShort(2)) - 5), "UTF-8").replaceAll("\0", ", ");
                    message =  ("Connected users: " + nicknames);
                }catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                break;
            case OpCodes.QUIT:
                System.out.println("Message wants to quit you");
                OriginType = 1;
                message = "Server closed connection";
                break;
            case OpCodes.UJOIN:
                System.out.println("Found user-joined message");
                OriginType = 2;
                nickLength = (int)PDUData.getByte(1);
                time = (int)PDUData.getInt(4);
                try {
                    nick = new String(PDUData.getSubrange(8, nickLength), "UTF-8");
                }catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                message = nick + " joined the room.";
                break;
            case OpCodes.ULEAVE:
                System.out.println("Found user-leaved message");
                OriginType = 1;
                nickLength = (int)PDUData.getByte(1);
                time = (int)PDUData.getInt(4);
                try {
                    nick = new String(PDUData.getSubrange(8, nickLength), "UTF-8");
                }catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                message = nick + " left the room.";
                break;
            case OpCodes.UCNICK:
                System.out.println("Found user-changed-nick message");
                OriginType = 3;
                nickLength = (int)PDUData.getByte(1);
                int nickLength2 = (int)PDUData.getByte(2);
                time = (int)PDUData.getInt(4);
                String newNick = "";
                try {
                    nick = new String(PDUData.getSubrange(8, nickLength), "UTF-8");
                    newNick = new String(PDUData.getSubrange(8 + Message.div4(nickLength), nickLength2), "UTF-8");
                }catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                message =  nick + " changed nick to: " + newNick;
                break;
        }
    }

    private void Message(){
        int checksum = PDUData.getByte(3);

        int nickLength, msgLength;
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
                    message = new String(deCrypt(PDUData.getSubrange(12, msgLength), Tabid), "UTF-8");
                    break;
                case 3:
                    System.out.println("HITTADE KOMPRIMERAD OCH KRYPTERAT MEDDELANDE");
                    try {
                        message = new String(deCompress(deCrypt(PDUData.getSubrange(12, msgLength), Tabid)), "UTF-8");
                    }catch(Exception e){
                        System.out.println("Decompression/decryption didn't work. Same key?");
                        message = "INVALID KEY";
                    }
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

    public int getOriginType(){
        return OriginType;
    }

    public int getTime(){
        return time;
    }

    public void setOp(int op){
        this.op = op;
    }

    public int getOp(){
        return op;
    }

    public byte[] getBytes(){
        return PDUData.getBytes();
    }

    private static byte[] deCrypt(byte[] cryptMsg, int Tabid){
        PDU encryptedPDU = new PDU(cryptMsg, cryptMsg.length);
        byte[] encryptedMsg = null;
        try {
            int algorithm = encryptedPDU.getByte(0);
            int checksum = encryptedPDU.getByte(1);
            int cryptLength = encryptedPDU.getShort(2);
            int unCryptLength = encryptedPDU.getShort(4);
            encryptedMsg = encryptedPDU.getSubrange(12, cryptLength);
            Crypt.decrypt(encryptedMsg, encryptedMsg.length, catalogue.getKey(Tabid), catalogue.getKey(Tabid).length);
            if(encryptedMsg.length != unCryptLength){
                encryptedMsg = "INVALID KEY".getBytes();
            }
        }catch(Exception e){
            System.out.println("Someone decrypted a message wrong");
        }
        return encryptedMsg;
    }
    private static byte[] deCompress(byte[] comprMsg){
        PDU compressedPDU = new PDU(comprMsg, comprMsg.length);
        int algorithm = compressedPDU.getByte(0);
        int checksum = compressedPDU.getByte(1);
        int compLength = compressedPDU.getShort(2);
        int unCompLength = compressedPDU.getShort(4);
        System.out.println("compressedPDU: '" + compressedPDU.length() + "'compLength: '" + compLength + "' unCompLength: '" + unCompLength + "'");
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
