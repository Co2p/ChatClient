package eson.co2p.se;

import javax.swing.*;
import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by gordon on 08/10/14.
 * Remembers states and identifiers
 */
public class catalogue {

    private static int idNumber;
    private static String nick = "Jesus";
    private static startGui gui = null;
    private static Server nameServer = new Server();
    private static Server thisServer = new Server();
    private static String key;
    public static ArrayList<String> Chatsync = new ArrayList<String>();
    public static String[] lol = new String[256];
    public static boolean MessageInUse = false;
    private static boolean firstAcess = true;

    catalogue(){}



    /**
     * sets an message at an position
     * @param Message the new message
     * @param Index the position to set(coresponding to the clientthread id)
     */
    public static void SetClientMessage (String Message, int Index) {
        firstFillArrayList();
        MessageInUse = true;
        lol[Index] = Message;
        //Chatsync.set(Index,Message);
        System.out.println("Setting message");
        MessageInUse = false;
    }

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

    /**
     * Sets the key value
     * @param key the value of the key
     */
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

    /**
     * Returns the key as bytes
     * @return the key as bytes
     */
    public static byte[] getKey(){
        return key.getBytes();
    }

    /**
     * Returns the key as a String
     * @return the key as a String
     */
    public static String getKeyString(){
        return key;
    }

    /**
     * returns the message to send, sets the position to null
     * @param Index the position to set(coresponding to the clientthread id)
     */
    public static String GetClientMessage (int Index) {
        firstFillArrayList();
        if(firstAcess == false) {
            MessageInUse = true;
            String ret = lol[Index];
            lol[Index] = null;
            //String ret = Chatsync.get(Index);
            //Chatsync.set(Index, null);
            MessageInUse = false;
            return ret;
        }
        else{
            return " ";
        }
    }

    private static void firstFillArrayList(){
        if(firstAcess == true) {
            //for (int i = 0; i < 256; i++) {
            //    Chatsync.add(i,null);
            //}
            for(int i = 0; i < lol.length; i++){
                lol[i] = null;
            }
            System.out.println("Filled Arraylist");
            firstAcess = false;
        }
    }

}