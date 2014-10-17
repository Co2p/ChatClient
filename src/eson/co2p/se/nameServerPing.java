package eson.co2p.se;

import java.net.*;
import java.util.ArrayList;

/**
 * Created by gordon on 07/10/14.
 *
 */


public class nameServerPing {

    private ArrayList<Integer> format = new ArrayList<Integer>();
    private ArrayList<Object> content = new ArrayList<Object>();
    byte[] outputStream;

    private void fillArrayLists(){
        outputStream  = Message.getServerMessage();
        System.out.println("Length of outputStream = " + outputStream.length);
        //kolla på råbinärkoden
        for (byte b : outputStream) {
            System.out.println(Integer.toBinaryString(b & 255 | 256).substring(1));
        }
    }

    public ServerList getUdpServerlist() throws Exception{
        fillArrayLists();
        System.out.println("Filled array list!");

        //BufferedReader nameserverAnswere = new BufferedReader(new InputStreamReader("OP: 1"));
        //String nameserverAnswere = LOL ;
        DatagramSocket clientSocket = new DatagramSocket();

        byte[] sendData = new byte[65507];
        byte[] receiveData = new byte[65507];
        //String sentence = nameserverAnswere.readLine();
        //sendData = nameserverAnswere.getBytes();
        //sendData = loll;
        sendData = outputStream;
        //sendData = nameserverAnswere.getBytes();

        try {
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, catalogue.getNameServerInet(), catalogue.getNameServerPort());
            clientSocket.send(sendPacket);
        }catch (Exception e){
            System.out.print("Failed to send packet");
            e.printStackTrace();
        }

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        //Här hanteras all inläsning av serverns utskick av serverdata som är kopplade
        //Till namnservern
        ServerList servers = new ServerList(receivePacket.getData());
        ArrayList serverlist = servers.getServerList();
        //Print all info on servers, this is just for testing purposes
        ArrayList ArrayOfServers = new ArrayList();
        InetAddress TempAdress;
        for(int i = 0; i < serverlist.size(); i++){
            TempAdress = servers.getServer((String)serverlist.get(i)).getIp();
            System.out.println("---" + servers.getServer((String)serverlist.get(i)).getName() + "---");
            System.out.println("ip: " + servers.getServer((String)serverlist.get(i)).getIp());
            System.out.println("port: " + servers.getServer((String)serverlist.get(i)).getPort());
            System.out.println("connected clients: " + servers.getServer((String)serverlist.get(i)).getConnected());
            ArrayOfServers.add(TempAdress);
        }
        clientSocket.close();
        return servers;
    }

}
