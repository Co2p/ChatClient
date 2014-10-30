package eson.co2p.se;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;

/**
 * Remembers states and identifiers
 * @author Gordon, Isidor, Tony 23 October 2014
 */
public class catalogue {

    private static int idNumber;
    private static String nick = "Nick";
    private static startGui gui = null;
    private static Server nameServer = new Server();
    private static Server thisServer = new Server();
    private static String key = "foobar";
    public static ArrayList<String> Chatsync = new ArrayList<String>();
    public static String[] message = new String[256];
    public static String[] Keys = new String[256];
    public static boolean[] comp = new boolean[256];
    public static boolean[] crypt = new boolean[256];
    public static boolean[] dosreq = new boolean[256];
    public static boolean MessageInUse = false;
    private static boolean firstAcess = true;
    private static boolean manualServer = false;

    private static Hashtable UsenameHashList = new Hashtable();



    /**
     * sets an message at an position
     * @param Message the new message
     * @param Index the position to set(coresponding to the clientthread id)
     */
    public static void SetClientMessage (String Message, int Index, boolean compr, boolean crypto) {
        firstFillArrayList();
        MessageInUse = true;
        message[Index] = Message;
        comp[Index] = compr;
        crypt[Index] = crypto;
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

    public static void setNicknames(String[] nicks, int TabID){

        ArrayList<String> NicksAsArraylist = new ArrayList<String>(Arrays.asList(nicks));

        if (!UsenameHashList.contains(TabID)){
            UsenameHashList.put(TabID,NicksAsArraylist);
        }

    }

    public static void setNicknames(String nick, int TabID){

        ArrayList<String> Temparray = (ArrayList<String>) UsenameHashList.get(TabID);
        if(!Temparray.contains(nick)){
            Temparray.add(nick);
            UsenameHashList.put(TabID,Temparray);
        }

    }

    /**
     * Sets the key value
     * @param key the value of the key
     */
    public static void setKey(String key){
        catalogue.key = key;
    }

    //##############################################
    //The divide line between setters and getters  #
    //##############################################
    
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

    //
    public static void SetDosReq(int index){
        dosreq[index] = true;
    }

    public static boolean Updatedosreq(int index){
        boolean ret = dosreq[index];
        if(dosreq[index]){
            dosreq[index] = false;
        }
        return ret;
    }

    /**
     * Decides whether the client should connect to a server directly or via a nameserver
     * @param ManualSrv True if the client connects directly to a server
     */
    public static void setmanualServer(boolean ManualSrv){
        manualServer=ManualSrv;
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
    public static byte[] getKey(int Index){

        firstFillArrayList();
        if(!firstAcess) {
            String ret = Keys[Index];
            return ret.getBytes();
        }
        else{
            return "foobar".getBytes();
        }
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
     *
     */
    public static String GetClientMessage (int Index) {
        firstFillArrayList();
        if(!firstAcess) {
            MessageInUse = true;
            String ret = message[Index];
            message[Index] = null;
            //String ret = Chatsync.get(Index);
            //Chatsync.set(Index, null);
            MessageInUse = false;
            return ret;
        }
        else{
            return " ";
        }
    }

    /**
     * returns if using crypting or not
     * @param Index the position to get(coresponding to the clientthread id)
     *
     */
    public static boolean GetCrypt (int Index) {
        firstFillArrayList();
        return !firstAcess && crypt[Index];
    }
    /**
     * returns if using compression or not or not
     * @param Index the position to get(coresponding to the clientthread id)
     *
     */
    public static void SetcryptKey(int Index, String key){
        firstFillArrayList();
        Keys[Index] = key;
    }
    /**
     * returns if using compression or not or not
     * @param Index the position to get(coresponding to the clientthread id)
     *
     */
    public static String GetCryptKey(int Index){
        firstFillArrayList();
        if(!firstAcess) {
            return Keys[Index];
        }
        else{
            return "foobar";
        }
    }
    /**
     * returns if using compression or not or not
     * @param Index the position to get(coresponding to the clientthread id)
     *
     */
    public static boolean GetComp (int Index) {
        firstFillArrayList();
        return !firstAcess && comp[Index];
    }

    private static void firstFillArrayList(){
        if(firstAcess) {
            for(int i = 0; i < message.length; i++){
                message[i] = null;
                comp[i] = false;
                crypt[i] = false;
                dosreq[i] = false;
                Keys[i] = "foobar";
            }
            firstAcess = false;
        }
    }

    public static String getNicknames(int TabID) {

        ArrayList<String> Temparray = (ArrayList<String>) UsenameHashList.get(TabID);



        String tempNicks = "";

        if (Temparray.get(TabID).equals(1)){
            return "There's no one here but you!";
        }
        for (int i=0; i< Temparray.size(); i++) {
            System.out.println("Temp Array: " + Temparray );
            System.out.println("Array lenght: " + Temparray.size() );
            tempNicks = tempNicks + Temparray.get(i) + ", ";
        }
        return tempNicks;
    }

    /**
     * Decides whether the client should connect to a server directly or via a nameserver
     * @return True if the client connects directly to a server
     */
    public static boolean getmanualServer(){
        return manualServer;
    }

    public static void removeNickname(String nick, int TabID) {

        ArrayList<String> Temparray = (ArrayList<String>) UsenameHashList.get(TabID);

        if(Temparray.contains(nick)){
            Temparray.remove(Temparray.indexOf(nick));
            UsenameHashList.put(TabID, Temparray);
        }
    }

}