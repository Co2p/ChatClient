package eson.co2p.se;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Isidor on 2014-10-13.
 *
 * Keeps track of the servers that were returned from the name server
 */
public  class ServerList {

    //Array of the names of the servers given by the nameserver
    //Used when the client needs a serverip from the namehash
    ArrayList<String> serverNames= new ArrayList<String>();
    //Namehash used to store the ipaddress relative to servername
    Hashtable <String, Server> ipHash = new Hashtable <String, Server>();
    int operation, sequenze, chatServers, tot;

    /**
     * Creates a serverList with all of the servers that the name server returned
     * @param message the returned header from the name server
     */
    public ServerList(String ip, int Por){
        Server Server = new eson.co2p.se.Server();
        InetAddress adre = null;
        try {
            adre = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Server.setIp(adre);
        Server.setPort(Por);
        Server.setConnected(6);
        Server.setName("Direct connect");
        ipHash.put(Server.getName(), Server);
        serverNames.add(Server.getName());


    }
    public ServerList(byte[] message){
        PDU reMessage = new PDU(message, message.length);
        operation = (int)reMessage.getByte(0);
        sequenze = (int) reMessage.getByte(1);
        chatServers = reMessage.getShort(2);
        tot = 4;
        System.out.println(message.toString());
        //For each server, create a serverobject and add to serverhash
        for(int i = 0; i < chatServers; i++) {
            Server Server = new eson.co2p.se.Server();
            Server.setIp(getIp(reMessage.getSubrange(tot, 4)));
            Server.setPort(reMessage.getShort(tot + 4));
            Server.setConnected(reMessage.getByte(tot + 6));
            int serverNameLength = (int) reMessage.getByte(tot + 7);
            try {
                Server.setName(new String(reMessage.getSubrange(tot + 8, serverNameLength), "UTF-8"));
            }catch(UnsupportedEncodingException e){
                System.out.println("Unsupported coding exception: " + e);
            }

            ipHash.put(Server.getName(), Server);
            serverNames.add(Server.getName());
            //Check that the total length of the namebytes is modulus 4
            tot += 8 + div4(serverNameLength);
        }
    }

    /**
     * Returns a server object
     * @param ServerName Serverns namn
     * @return The server object
     */
    public Server getServer(String ServerName){
        return ipHash.get(ServerName);
    }

    /**
     * Prints the servers in the array, this is just for testing purposes and
     * should NOT be used in the final program
     */
    public void printServers(){
        InetAddress TempAdress;
        for(int i = 0; i < serverNames.size(); i++){
            TempAdress = getServer((String)serverNames.get(i)).getIp();
            System.out.println("---" + getServer((String)serverNames.get(i)).getName() + "---");
            System.out.println("ip: " + getServer((String)serverNames.get(i)).getIp());
            System.out.println("port: " + getServer((String)serverNames.get(i)).getPort());
            System.out.println("connected clients: " + getServer((String)serverNames.get(i)).getConnected());
        }
    }

    /**
     * Returns all of the available server names
     * @return server names
     */
    public ArrayList<String> getServerList(){
        return serverNames;
    }

    private InetAddress getIp(byte[] bytes){
        try {
            return InetAddress.getByAddress(bytes);
        }catch(UnknownHostException e){
            System.out.println("Unknown Host Exception occurred: " + e);
        }
        return null;
    }

    /**
     * //TODO explain
     * @param testInt
     * @return
     */
    public static int div4(int testInt){
        int ret = 0;
        if((4 -(testInt % 4)) != 0){
            ret = (4 -(testInt % 4));
        }
        return testInt + ret;
    }
}
