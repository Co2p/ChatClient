package eson.co2p.se;

import java.io.*;
import java.net.InetAddress;
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
    //private BufferedReader inFromServer;
    private DataInputStream in;
    String receivedData;
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
        System.out.println("Starting message check!");
        checkReceivedMessage();

    }

    //connect the socket to given ip/port
    private void ConnectSocket(){
        int tries = 0;
        int maxTries = 5;
        while(tries < maxTries) {
            try {
                localServerSocket = new Socket(Ip, Port);
                localServerSocket.setSoTimeout(500);
                in = new DataInputStream(localServerSocket.getInputStream());
                //inFromServer = new BufferedReader(new InputStreamReader(localServerSocket.getInputStream()));
                System.out.println("connected socket!");
                tries = maxTries;
            } catch (IOException e) {
                tries += 1;
                System.out.println("Failed to bind socket, try " + tries + "/" + maxTries);
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
            System.out.println("Sending message....");
            outToServer.write(Message.sendMessage(messageToSend, Type));
        }catch (IOException e){
            System.out.println("Failed to send message");
        }
    }
    //chek if this thread is still alive
    public boolean endSocketCheck(){
        int myLife = ClientThread.AliveThreadsID[Tabid];
        if (myLife == 1){
            return true;
        }
        else{
            return false;
        }
    }


    private String GetMessageToSend(){
        String Message;
        while(true){
            if(!catalogue.MessageInUse){
                Message = catalogue.GetClientMessage(Tabid);
                System.out.println("The message (GetMessageToSend): " + Message);
                if (Message != null){
                    System.out.println("Message broken, sorry bro...");
                }
                break;
            }
        }
        return Message;
    }
    //check for received messages on this socket
    private void checkReceivedMessage(){
        boolean goOn = true;
        boolean firstRun = true;
        int totBytesRead = 0;
        String messageString = "";
        byte[] messageByte = new byte[1000];
        int totLength = 0;
        PDU message = new PDU(0);

        while(endSocketCheck() && goOn) {
            totBytesRead = 0;
            totLength = 0;
            System.out.println("In while-loop");
            try {
                int bytesRead = in.read(messageByte);
                totBytesRead += bytesRead;
                if(bytesRead > 8) {
                    //TODO antar bara här att det är ett meddelande som tas emot, ISIDOR KOM IHÅG FÖR FAN DIN JÄVLA BÖG
                    PDU temp = new PDU(messageByte, messageByte.length);
                    System.out.println("OP-code: " + (int)temp.getByte(0));
                    if((int)temp.getByte(0) == OpCodes.MESSAGE) { //kollar op
                        //firstRun = false;
                        System.out.println("Found message!");
                        totLength = 12;
                        totLength += (int)temp.getByte(2);
                        totLength += temp.getShort(4);
                        System.out.println("nickLength: " + (int)temp.getByte(2) + ", messageLength: " + (int)temp.getByte(4));
                        totLength = Message.div4(totLength);
                        message.extendTo(totLength);
                    }else if((int)temp.getByte(0) == OpCodes.NICKS){
                        //firstRun = false;
                        System.out.println("Found nicks!");
                        totLength = 4;
                        totLength += Message.div4(temp.getShort(2));
                        message.extendTo(totLength);
                    }
                    //TODO detta är just nu en jävla skräphög, fixa in det här i fina metoder osv sen
                    System.out.println("totLength = " + totLength + ", bytesRead = " + bytesRead +
                            ", totBytesRead = " + totBytesRead + ", length of byte-array = " + messageByte.length);
                    message.setSubrange(0, temp.getSubrange(0,totLength));
                    if (totBytesRead >= totLength && (int)temp.getByte(0)==OpCodes.NICKS) {
                        String nicknames = new String(message.getSubrange(4,totLength-5), "UTF-8");
                        nicknames = nicknames.replaceAll("\0", "\n");
                        System.out.println("Connected users: " + nicknames);
                        GUI.UpdateTabByID(Tabid, "Connected users:\n" + nicknames);
                    }else if(totBytesRead >= totLength && (int)temp.getByte(0)==OpCodes.MESSAGE){
                        String nick = new String(message.getSubrange(12 + temp.getShort(4), (int)temp.getByte(2)), "UTF-8");
                        String message1 = new String(message.getSubrange(12,temp.getShort(4)), "UTF-8");
                        GUI.UpdateTabByID(Tabid, nick + ":" + message1);
                    }
                    if(bytesRead > totLength){
                        System.out.println(temp.getSubrange(totLength, bytesRead));
                    }
                }else{
                    //Hittas inget, så törna in mannen
                    //TODO Här ska inget törnas in, fucking fel?!?!?!?!?!
                    try {
                        System.out.println("Current Thread: " + Thread.currentThread());
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("not able to sleep: " + e);
                    }
                }
            }catch(IOException e){
                //Read time out, this should happen once every one second.
                //System.out.println(e);
            }


            /*goOn = false;

            System.out.println("checkReceivedMessage start");
            String Mess = GetMessageToSend();
            if(Mess != null){
                sendMessage(Mess,0);
                System.out.println("Sent message:" + Mess);
            }
            try {
                try {
                    receivedData = inFromServer.readLine();
                    String Message = receivedData;
                    System.out.println("Received message: " + Message);
                    while(receivedData != null) {
                        receivedData = inFromServer.readLine();
                        Message = Message + receivedData;
                    }
                    if (Message != null){
                        goOn = true;
                    }
                }catch (java.net.SocketTimeoutException e){
                    System.out.println("Socket timeout (Don't worry)");
                }
                if (goOn == true) {
                    System.out.println("Updating gui WITH MESSAGE: " + receivedData);
                    GUI.UpdateTabByID(Tabid, receivedData);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        System.out.println("not able to sleep: " + e);
                    }
                }
            } catch (IOException e) {
                System.out.println("Failed to get message");
            }*/
        }
        System.out.println("MESSAGE: " + messageString);
        /*
        try {
            outToServer.write(Message.quitServer());
            localServerSocket.close();
            System.out.println("Closing this instance of SenderServer");
        }catch (IOException e){
            System.out.println("Failed to close socket");
        }*/
    }
}
