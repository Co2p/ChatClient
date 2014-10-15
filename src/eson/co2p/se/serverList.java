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
public class serverList {

    //Array of the names of the servers given by the nameserver
    //Used when the client needs a serverip from the namehash
    ArrayList<String> serverNames= new ArrayList<String>();
    //Namehash used to store the ipaddress relative to servername
    Hashtable <String, server> ipHash = new Hashtable <String, server>();
    int operation, sequenze, chatServers, tot;

    /**
     * Creates a serverList with all of the servers that the name server returned
     * @param message the returned header from the name server
     */
    public serverList(byte[] message){
        PDU reMessage = new PDU(message, message.length);
        operation = (int)reMessage.getByte(0);
        sequenze = (int) reMessage.getByte(1);
        chatServers = reMessage.getShort(2);
        tot = 4;
        //For each server, create a serverobject and add to serverhash
        for(int i = 0; i < chatServers; i++) {
            server Server = new server();
            Server.setIp(byteConverter.getIp(reMessage.getSubrange(tot, 4)));
            tot += 4;
            Server.setPort(reMessage.getShort(tot));
            tot += 2;
            Server.setConnected(reMessage.getByte(tot));
            tot += 1;
            int serverNameLength = (int) reMessage.getByte(tot);
            tot += 1;
            try {
                Server.setName(new String(reMessage.getSubrange(tot, serverNameLength), "UTF-8"));
            }catch(UnsupportedEncodingException e){
                System.out.println("Unsupported coding exception: " + e);
            }

            ipHash.put(Server.getName(), Server);
            serverNames.add(Server.getName());
            //Check that the total length of the namebytes is modulus 4
            tot += byteConverter.div4(serverNameLength);
        }
    }

    /**
     * Returns a server object
     * @param ServerName Serverns namn
     * @return The server object
     */
    public server getServer(String ServerName){
        return ipHash.get(ServerName);
    }

    /**
     * Returns all of the available server names
     * @return server names
     */
    public ArrayList<String> getServerList(){
        return serverNames;
    }
}
