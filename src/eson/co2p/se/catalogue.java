package eson.co2p.se;

import java.net.InetAddress;

/**
 * Created by gordon on 08/10/14.
 * Remembers states and identifiers
 */
public class catalogue {

    private static int idNumber;
    private static String nick;
    private static Server nameServer = new Server();
    private static Server thisServer = new Server();
    private static int messageType;
    private static String key;

    catalogue(){}

    /**
     * Sets the ID number that the client received from the server
     * @param idNumber the ID number
     */
    public static void setIdNumber(int idNumber) {
        catalogue.idNumber = idNumber;
    }

    /**
     * Sets the user name
     * @param nick user name
     */
    public static void setName(String nick) {
        catalogue.nick = nick;
    }

    /**
     * Set the name server properties
     * @param ip server address
     * @param port server port
     */
    public static void setNameServer(InetAddress ip, int port){
        nameServer.setIp(ip);
        nameServer.setPort(port);
    }

    /**
     * Set the this servers properties
     * @param ip server adress
     * @param port server port
     */
    private static void setServer(InetAddress ip, int port){
        thisServer.setIp(ip);
        thisServer.setPort(port);
    }

    /**
     * Sets the current message type
     * @param messageType
     */
    public static void setMessageType(int messageType){
        catalogue.messageType=messageType;
    }

    public static void setKey(String key){
        catalogue.key = key;
    }

    //The divide line between setters and getters

    /**
     * Returns the client ID number
     * @return ID number
     */
    public static int getIdNumber() {
        return idNumber;
    }

    /**
     * Returns the user name
     * @return user name
     */
    public static String getNick() {
        return nick;
    }

    /**
     * Returns the nameserver as a server object
     * @return the server object
     */
    public static Server getNameServer(){
        return nameServer;
    }

    /**
     * Returns only the InetAddress for the name server
     * @return InetAddress
     */
    public static InetAddress getNameServerInet(){
        return nameServer.getIp();
    }

    /**
     * Returns only the port for the name server
     * @return name server port
     */
    public static int getNameServerPort(){
        return nameServer.getPort();
    }

    /**
     * Returns the message type as a int (as defined by MsgTypes)
     * @return message type
     */
    public static int getMessageType(){
        return messageType;
    }

    /**
     * Returns this server as a server object
     * @return this server object
     */
    public static Server getServer(){
        return thisServer;
    }

    /**
     * Returns this servers InetAddress
     * @return this servers InetAddress
     */
    public static InetAddress getServerInet(){
        return thisServer.getIp();
    }

    /**
     * Returns this servers active port
     * @return this servers port
     */
    public static int getServerPort(){
        return thisServer.getPort();
    }

    public static byte[] getKey(){
        return key.getBytes();
    }

    public static String getKeyString(){
        return key;
    }

}