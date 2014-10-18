package eson.co2p.se;

import java.net.InetAddress;

/**
 * Created by gordon on 08/10/14.
 * Remembers states and identifiers
 */
public class catalogue {

    private static int idNumber;
    private static String nick;
    private static startGui gui = null;
    private static Server nameServer = new Server();
    private static Server thisServer = new Server();
    private static String key;

    catalogue(){}

    /**
     * Sets the Gui
     * @param Gui the Gui to set
     */
    public static void setGui (startGui Gui) {
        catalogue.gui = Gui;
    }
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

    public static void setKey(String key){
        catalogue.key = key;
    }

    //The divide line between setters and getters

    /**
     * rerurns the existing GUI, if none exsists return null
     * @return Gui or null
     */
    public static startGui getGui () {
        if(catalogue.gui != null){
            return catalogue.gui;
        }
        else{
            return null;
        }
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