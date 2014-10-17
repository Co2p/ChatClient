package eson.co2p.se;

import java.net.InetAddress;

/**
 * Created by gordon on 08/10/14.
 * Remembers states and identifiers
 */
public class catalogue {

    private static int idNumber;
    private static String nick;
    private static server nameServer = new server();
    private static server thisServer = new server();
    private static int messageType;

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
    public static server getNameServer(){
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
    public static server getServer(){
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

}