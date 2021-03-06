package eson.co2p.se;

import java.net.*;
import java.util.ArrayList;

/**
 * Retrieves the active servers from the name server
 *
 * @author Gordon, Isidor, Tony 24 October 2014
 */

public class UDPServerConnection {
    /**
     * Sends a getserverlist message to the nameserver from catalogue, and then
     * handles the return from the server.
     *
     * @return  a list of all servers returned by the nameservers
     * @throws Exception
     */
    public static ServerList getUdpServerlist() throws Exception{
        DatagramSocket clientSocket = new DatagramSocket();
        byte[] sendData = Message.getServerMessage();
        byte[] receiveData = new byte[65507];
        ArrayList<Integer> format = new ArrayList<Integer>();
        ArrayList<Object> content = new ArrayList<Object>();

        //Tests to send a getservermessage to the given nameserver
        try {
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, catalogue.getNameServerInet(), catalogue.getNameServerPort());
            clientSocket.send(sendPacket);
        }catch (Exception e){
            System.out.print("Failed to send packet");
            e.printStackTrace();
        }

        //Waits and checks incoming data
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        ServerList servers = new ServerList(receivePacket.getData());
        ArrayList serverlist = servers.getServerList();

        clientSocket.close();
        return servers;
    }
}
