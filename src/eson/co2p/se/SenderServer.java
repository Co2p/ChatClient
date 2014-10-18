package eson.co2p.se;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by gordon on 08/10/14 modded by tony 18/10/14 .
 * Now made to be threadede
 * TCP connection to the Chat Server
 */
public class SenderServer{
    int Port;
    InetAddress Ip;
    private Socket localServerSocket;
    private PrintStream outToServer;
    private BufferedReader inFromServer;
    String recivedData;
    startGui GUI;
    int Tabid;

    public SenderServer(InetAddress IP,int port,int TabId){
        Ip = IP;
        Port = port;
        Tabid = TabId;
        System.out.println("Setting connection whit ip:" + Ip +"and port:" + Port + "on table:" + Tabid);
        ConnectSocket();
        System.out.println("getting working GUI");
        GUI = catalogue.getGui();
        System.out.println("Starting message chek!");
        checkReciveMessage();
    }

    //connect the socket to given ip/port
    private void ConnectSocket(){
        int Tryes = 0;
        int MaxTryes = 5;
        while(Tryes < MaxTryes) {
            try {
                localServerSocket = new Socket(Ip, Port);
                localServerSocket.setSoTimeout(1000);
                System.out.println("connected socket!");
                Tryes = MaxTryes;
            } catch (IOException e) {
                Tryes += 1;
                System.out.println("Failed to bind socket, try " + Tryes + "/" + MaxTryes);
            }
        }

        try {
            outToServer = new PrintStream(localServerSocket.getOutputStream(), true);
            //skicka anslutningsmedelande
            outToServer.write(Message.connectToServerMessage());

        }catch (IOException e){
            System.out.println("Failed to send registration message");
        }
    }
    //send message on the connected socket
    public void sendMessage(String messageToSend,int Type){
        try{
            outToServer.write(Message.sendMessage(messageToSend, Type));
        }catch (IOException e){
            System.out.println("Failed to send message");
        }
    }
    //chek if this thread is still alive
    public boolean endSocketChek(){
        int myLife = ClientThread.AliveThreadsID[Tabid];
        if (myLife == 1){
            return true;
        }
        else{
            return false;
        }
    }

    //chek for recived messages on this socket
    private void checkReciveMessage(){
        while(endSocketChek()) {
            try {
                boolean Goon = false;
                try {
                    inFromServer = new BufferedReader(new InputStreamReader(localServerSocket.getInputStream()));
                    recivedData = inFromServer.readLine();
                    Goon = true;
                }catch (java.net.SocketTimeoutException e){
                    System.out.println("Timed out trying to read from socket(this is supose to happend once every sec)");
                }
                if (Goon == true) {
                    System.out.println("Updating gui WHIT MESSAGE: " + recivedData);
                    GUI.UpdateTabByID(Tabid, recivedData);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        System.out.println("not able to sleep: " + e);
                    }
                }
            } catch (IOException e) {
                System.out.println("Failed to get message");
            }
        }
        try {
            outToServer.write(Message.quitServer());
            localServerSocket.close();
            System.out.println("Closing this instance of SenderServer");
        }catch (IOException e){
            System.out.println("Failed to close socket");
        }
    }
}
