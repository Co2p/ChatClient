package eson.co2p.se;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
/**
 * TCP connection to the Chat Server
 * Now made to be threaded
 *
 * Created by gordon on 08/10/14 modded by tony 18/10/14.
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
    /**
     * Constructs a TCP server on/to the given ip and port
     * @param IP a InetAddress ip for the requested chatserver
     * @param port a portnumber for the requested chatserver
     * @param TabId the id-number of the associated tab
     */
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
    /**
     * Connect the socket to given ip/port, as defined in the constructor
     */
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
    /**
     * Send message over the connected socket
     * @param messageToSend the message
     * @param Type defines what type the message is according to the MsgTypes.class
     * @see eson.co2p.se.MsgTypes
     */
    public void sendMessage(String messageToSend,int Type){
        try{
            System.out.println("Sending message....");
            outToServer.write(Message.sendMessage(messageToSend, Type));
        }catch (IOException e){
            System.out.println("Failed to send message");
        }
    }
    /**
     * checks if this thread is still alive
     * @return true if it is alive
     */
    public boolean endSocketCheck(){
        int myLife = ClientThread.AliveThreadsID[Tabid];
        if (myLife == 1){
            return true;
        }
        else{
            return false;
        }
    }
    //TODO comment?
    private String GetMessageToSend(){
        String Message;
        while(true){
            if(!catalogue.MessageInUse){
                Message = catalogue.GetClientMessage(Tabid);
//System.out.println("The message (GetMessageToSend): " + Message);
                if (Message != null){
                    System.out.println("Message broken, sorry bro...no message is not broken, yes it is!");
                }
                break;
            }
        }
        return Message;
    }
    /**
     * Checks for received messages on this socketasd
     */
    private void checkReceivedMessage(){
        boolean goOn = true;
        boolean firstRun = true;
        int totBytesRead, totLength;
        String messageString = "";
        byte[] messageByte = new byte[1000];
        PDU message = new PDU(0);
        while(endSocketCheck() && goOn) {
            String Messagelol = GetMessageToSend();
            if (Messagelol != null){
                System.out.println("message to send:" + Messagelol);
                sendMessage(Messagelol,0);
            }
            try {
                int bytesRead = in.read(messageByte);
                if(bytesRead > 8) {
                    PDU temp = new PDU(messageByte, messageByte.length);
                    RecMessageBreakDown(temp);
                }else{
//Hittas inget, så törna in mannen
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("not able to sleep: " + e);
                    }
                }
            }catch(IOException e){
//Read time out, this should happen once every one second.
//System.out.println(e);
            }
        }
/*
try {
outToServer.write(Message.quitServer());
localServerSocket.close();
System.out.println("Closing this instance of SenderServer");
}catch (IOException e){
System.out.println("Failed to close socket");
}*/
    }
    private RecMessage RecMessageBreakDown(PDU message){
//Checks op-codes and adds creates the correct message
        int opCode = (int)message.getByte(0);
        System.out.println("OP-code: " + opCode);
        RecMessage returnMes = null;
        int nickLength, time;
        String nick = "";
        switch(opCode){
            case OpCodes.MESSAGE:
                System.out.println("Found message!");
                RecMessage_Message temp = new RecMessage_Message(message.getBytes());
                if(temp.getNick().length() > 0) {
                    GUI.UpdateTabByID(Tabid, getTime(temp.getTime()) + ":" + temp.getNick() + ":" + temp.getMessage());
                }else{
                    GUI.UpdateTabByID(Tabid, getTime(temp.getTime()) + ":" + temp.getMessage());
                }
                returnMes = temp;
                break;
            case OpCodes.NICKS:
                System.out.println("Found nicks!");
                try {
                    String nicknames = new String(message.getSubrange
                            (4, Message.div4(message.getShort(2)) - 5), "UTF-8").replaceAll("\0", ", ");
                    GUI.UpdateTabByID(Tabid, "Connected users: " + nicknames);
                }catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                break;
            case OpCodes.QUIT:
                GUI.UpdateTabByID(Tabid, "Server Closed Connection");
                break;
            case OpCodes.UJOIN:
                nickLength = (int)message.getByte(1);
                time = (int)message.getInt(4);
                nick = "";
                try {
                    nick = new String(message.getSubrange(8, nickLength), "UTF-8");
                }catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                GUI.UpdateTabByID(Tabid, getTime(time) + ":" + nick + " Joined the room.");
                break;
            case OpCodes.ULEAVE:
                nickLength = (int)message.getByte(1);
                time = (int)message.getInt(4);
                nick = "";
                try {
                    nick = new String(message.getSubrange(8, nickLength), "UTF-8");
                }catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                GUI.UpdateTabByID(Tabid, getTime(time) + ":" + nick + " left the room.");
                break;
            case OpCodes.UCNICK:
                nickLength = (int)message.getByte(1);
                int nickLength2 = (int)message.getByte(2);
                time = (int)message.getInt(4);
                String newNick = "";
                try {
                    nick = new String(message.getSubrange(8, nickLength), "UTF-8");
                    newNick = new String(message.getSubrange(8 + nickLength, nickLength2), "UTF-8");
                }catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                GUI.UpdateTabByID(Tabid, getTime(time) + ":" + nick + " changed nick to: " + newNick);
                break;
        }
        return returnMes;
    }
    private String getTime(int time){
        System.out.println("TIME ITSELF: " + time);
        Date retTime = new Date(time*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        return sdf.format(retTime);
    }
}
