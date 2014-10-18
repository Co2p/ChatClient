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
        try {
            localServerSocket = new Socket(Ip, Port);
            System.out.println("connected socket!");
        }catch (IOException e){
            System.out.println("Failed to bind socket");
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
        int myLife = GUI.AliveThreadsID[Tabid];
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
                //BufferedInputStream input = new BufferedInputStream(localServerSocket.getInputStream());
                //ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //byte buffer[] = new byte[1024];
                inFromServer = new BufferedReader(new InputStreamReader(localServerSocket.getInputStream()));
                recivedData = inFromServer.readLine();
                //Nedan har isidor lagt in massa skit
                /*for(int s; (s=input.read(buffer)) != -1; )
                {
                    baos.write(buffer, 0, s);
                }
                RecMessage_Message meddelande = new RecMessage_Message(baos.toByteArray());*/

                System.out.println("Updating gui WHIT MESSAGE: " + recivedData);
                GUI.UpdateTabByID(Tabid,recivedData);
                try {
                    Thread.sleep(2);
                }catch (InterruptedException e){
                    System.out.println("not able to sleep: " + e);
                }
            } catch (IOException e) {
                System.out.println("Failed to get message");
            }
        }
        try {
            localServerSocket.close();
        }catch (IOException e){
            System.out.println("Failed to close socket");
        }
    }
}
