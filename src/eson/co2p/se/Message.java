package eson.co2p.se;

import java.io.*;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.zip.GZIPOutputStream;

/**
 * Includes methods for creating all the messages that's going to the server
 * from the server, also includes minor static math methods used in separate places
 *
 * @author Gordon, Isidor, Tony 23 October 2014
 *
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
        int usernameLength = div4(catalogue.getNick().getBytes().length);
        PDU rawdata = new PDU(4 + usernameLength);
        rawdata.setByte(0,(byte)OpCodes.JOIN);
        rawdata.setByte(1, (byte)usernameLength);
        //rawdata.setShort(2, (byte)0);
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
        rawdata.setByte(1, (byte)nickname.getBytes().length);
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
    public static byte[] sendMessage(String message, int type, int Tabid){
        PDU rawdata = null;
        try {
            byte[] msgByte = message.getBytes("UTF-8");
            switch(type){
                case 1:
                    msgByte = compress(msgByte);
                    break;
                case 2:
                    msgByte = enCrypt(msgByte,Tabid);
                    break;
                case 3:
                    msgByte = enCrypt(compress(msgByte),Tabid);
                    break;
            }
            rawdata = new PDU(12 + div4(msgByte.length));
            //  Adds the header
            rawdata.setByte(0, (byte) OpCodes.MESSAGE);
            rawdata.setByte(1,(byte)type);
            rawdata.setShort(4, (short)msgByte.length);
            rawdata.setSubrange(12, msgByte);
            //  Calculate the checkSum
            System.out.println("totLength: '" + rawdata.length() + "', Message length: '" + msgByte.length + "'");
            rawdata.setByte(3, Checksum.calc(rawdata.getBytes(), 12 + Message.div4(msgByte.length)));
        }catch(UnsupportedEncodingException e){
            System.out.println("Unsupported Encoding Exception: " + e);
        }
        return rawdata.getBytes();
    }

    /**
     * Encrypts the data with the given Crypt.java file and adds a crypt-header for it
     *
     * @param data  the data to be encrypted
     * @param Tabid used for getting the correct key connected to a specific server
     * @return      the encrypted data
     */
    private static byte[] enCrypt(byte[] data, int Tabid){
        int unCryptLength = data.length;
        Crypt.encrypt(data, data.length, catalogue.getKey(Tabid), catalogue.getKey(Tabid).length);
        PDU cryptPDU = new PDU(12 + div4(data.length));
        //  Adds the crypt-header
        cryptPDU.setByte(0, (byte)0);
        cryptPDU.setByte(1, Checksum.calc(data, data.length));
        cryptPDU.setShort(2, (short)data.length);
        cryptPDU.setShort(4, (short)unCryptLength);
        // Adds the newly encrypted data
        cryptPDU.setSubrange(8, data);
        cryptPDU.setByte(1, Checksum.calc(cryptPDU.getBytes(), cryptPDU.length()));
        return cryptPDU.getBytes();
    }

    /**
     * Compresses the data with the GZIP format and adds a GZIP-header
     *
     * @param data  the data to be compressed
     * @return      the compressed data
     */
    private static byte[] compress(byte[] data){
        byte[] returnData = null;
        int uncomplength = data.length;
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream(data.length);
            GZIPOutputStream gZip = new GZIPOutputStream(byteStream);
            gZip.write(data);
            gZip.close();
            byteStream.close();
            returnData = byteStream.toByteArray();
        }catch(Exception e){
            e.printStackTrace();
        }
        //  Adds the header
        PDU compPDU = new PDU(8 + div4(returnData.length));
        compPDU.setShort(2, (short) returnData.length);
        compPDU.setShort(4, (short) uncomplength);
        //  Adds the compressed data
        compPDU.setSubrange(8, returnData);
        compPDU.setByte(1, Checksum.calc(returnData, returnData.length));
        return compPDU.getBytes();
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
        if((testInt % 4) != 0){
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
