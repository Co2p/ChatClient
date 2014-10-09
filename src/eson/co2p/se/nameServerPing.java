package eson.co2p.se;

import com.sun.net.httpserver.HttpServer;

import java.io.*;
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
        format.add(1);
        format.add(3);
        content.add(2);
        content.add(null);
        int l = 3;
        outputStream  = byteConverter.headerBuilder(format,content);
    }

    public void getUdpServerlist() throws Exception{
        fillArrayLists();
        System.out.println("Filled array list!");
        int lol = 3;
        byte LOL = (byte)lol;
        byte[] loll = new byte[32];
        loll[0] = LOL;

        //BufferedReader nameserverAnswere = new BufferedReader(new InputStreamReader("OP: 1"));
        //String nameserverAnswere = LOL ;
        DatagramSocket clientSocket = new DatagramSocket();

        InetAddress IPAddress = InetAddress.getByName("itchy.cs.umu.se");
        //InetAddress lol = InetAddress.

        byte[] sendData = new byte[65507];
        byte[] receiveData = new byte[65507];
        //String sentence = nameserverAnswere.readLine();
        //sendData = nameserverAnswere.getBytes();
        //sendData = loll;
        sendData = outputStream;
        //sendData = nameserverAnswere.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 1337);
        //DatagramPacket sendPacket3 = new DatagramPacket();

        clientSocket.send(sendPacket);
        System.out.println("Got here!!");

        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        System.out.println("Got dsfgg");
        clientSocket.receive(receivePacket);
        System.out.println("Got here!ee!");
        String modifiedSentence = new String(receivePacket.getData());
        System.out.println("FROM SERVER:" + modifiedSentence);
        clientSocket.close();
        System.out.println("Got here");
    }







    /*
    private String ip, port;
    private int localPort = 1337;
    private ServerSocket serverSocket;
    private ArrayList<Integer> format;
    private ArrayList<String> content;
    private ArrayList<Byte> byteBuffer;
    */
    /**
     * Constructor
     * @param ip Server IP
     * @param port Server Port
     */
    /*
    public nameServerPing(String ip, String port){
        this.ip=ip;
        this.port=port;

        serverPing();
    }
    */
    /**
     * Sends a UDP request to the nameserver
     */
    /*
    public void serverPing(){
        try {
            serverSocket = new ServerSocket(localPort);
            Socket localSocket = serverSocket.accept();

                ByteArrayOutputStream out;
                try {
                    out = new ByteArrayOutputStream(4);
                    out.write(byteConverter.headerBuilder(format, content).toByteArray());
                } catch (IOException e) {
                    System.out.println("Error while sending request to nameserver");
                    e.printStackTrace();
                }

            BufferedReader listener = new BufferedReader(new InputStreamReader(localSocket.getInputStream()));

            Byte tempByte = (byte) listener.read();
            byteBuffer.add(tempByte);

            System.out.println(byteBuffer.get(0));

        } catch (IOException e) {
            System.out.println("Error while creating a port for nameserver");
            e.printStackTrace();
        }



    }
    */
}
