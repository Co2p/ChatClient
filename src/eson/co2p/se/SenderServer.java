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
        ConnectSocket();
        GUI = catalogue.getGui();
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
                localServerSocket.setSoTimeout(100);
                in = new DataInputStream(localServerSocket.getInputStream());
                tries = maxTries;
            } catch (IOException e) {
                tries += 1;
                System.out.println("Failed to bind socket, try " + tries + "/" + maxTries);
            }
        }
        try {
            outToServer = new PrintStream(localServerSocket.getOutputStream(), true);
            //  Skicka anslutningsmedelande
            outToServer.write(Message.connectToServerMessage());
        }catch (IOException e){
            System.out.println("Failed to send registration message");
            e.printStackTrace();
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
            System.out.println("Sending message: '" + messageToSend + "'");
            outToServer.write(Message.sendMessage(messageToSend, Type, Tabid));
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
        return myLife == 1;
    }

    /**
     * gets the message to send from the catalogue
     *
     * @return  the string to send
     */
    private String GetMessageToSend(){
        String Message;
        while(true){
            if(!catalogue.MessageInUse){
                Message = catalogue.GetClientMessage(Tabid);
                break;
            }
        }
        return Message;
    }

    /**
     * Gets the key from the catalogue, and checking the cryptationmethod
     *
     * @return
     */
    private int GetKey(){
        boolean crypt = catalogue.GetCrypt(Tabid);
        boolean comp = catalogue.GetComp(Tabid);
        if(!crypt && !comp){
            return 0;
        }
        else if(!crypt && comp){
            return 1;
        }
        else if(crypt && !comp){
            return 2;
        }
        else if(crypt && comp){
            return 3;
        }
        else{
            return 0;
        }
    }
    /**
     * Checks for received messages on this socket
     */
    private void checkReceivedMessage(){
        byte[] messageByte = new byte[1000];
        PDU message = new PDU(0);
        while(endSocketCheck()) {
            String Messagelol = GetMessageToSend();
            if (Messagelol != null){
                //Check if the message contains a command
                if(Messagelol.charAt(0) != '§'){
                    sendMessage(Messagelol, GetKey());
                }else{
                    System.out.println("Command found!");
                    byte[] command = Commands.getCommand(Messagelol, GUI, Tabid);
                    if(command != null && command.length > 4){
                        try {
                            outToServer.write(command);
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
            try {
                int bytesRead = in.read(messageByte);
                if(bytesRead > 8) {
                    PDU temp = new PDU(messageByte, messageByte.length);
                    RecMessageBreakDown(temp);
                }else{
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("not able to sleep: " + e);
                    }
                }

            }catch(IOException e){
                //don't put anything here as it will cramp the terminal
            }
        }
        //After endsocketcheck, disconnect from the server
        try {
            outToServer.write(Message.quitServer());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Breaks down a received message, checks the op-code and prints it accordingly
     *
     * @param message   The message to break down
     * @return  A created message (only used if message is message)
     */
    private RecMessage RecMessageBreakDown(PDU message){
        int opCode = (int)message.getByte(0);
        RecMessage returnMes = null;
        int nickLength, time;
        String nick = "";

        switch(opCode){
            case OpCodes.MESSAGE:
                System.out.println("Found message!");
                RecMessage_Message temp = new RecMessage_Message(message.getBytes(),Tabid);
                GUI.UpdateTabByID2(Tabid, getTime(temp.getTime()), temp.getNick(), temp.getMessage(), temp.getType(), 0);
                returnMes = temp;
                break;
            case OpCodes.NICKS:
                System.out.println("Found nicks!");
                try {
                    String nicknames = new String(message.getSubrange
                            (4, Message.div4(message.getShort(2)) - 5), "UTF-8").replaceAll("\0", ", ");
                    GUI.UpdateTabByID2(Tabid, null, null, "Connected users: " + nicknames, 0, 1);
                }catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                break;
            case OpCodes.QUIT:
                GUI.UpdateTabByID2(Tabid, null, null, "Server closed connection", 0, 1);
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
                GUI.UpdateTabByID2(Tabid, getTime(time), null, nick + " joined the room.", 0, 2);
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
                GUI.UpdateTabByID2(Tabid, getTime(time), null, nick + " left the room.", 0, 1);
                break;
            case OpCodes.UCNICK:
                nickLength = (int)message.getByte(1);
                int nickLength2 = (int)message.getByte(2);
                time = (int)message.getInt(4);
                String newNick = "";
                try {
                    nick = new String(message.getSubrange(8, nickLength), "UTF-8");
                    newNick = new String(message.getSubrange(8 + Message.div4(nickLength), nickLength2), "UTF-8");
                }catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                GUI.UpdateTabByID2(Tabid, getTime(time), null, nick + " changed nick to: " + newNick, 0, 3);
                break;
        }
        return returnMes;
    }

    /**
     * returns the time in simpleDataFormat for easier reading
     *
     * @param time  time in UNIX format
     * @return      time in simpleDataFormat
     */
    private String getTime(int time){
        System.out.println("TIME ITSELF: " + time);
        Date retTime = new Date(time*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("CET"));
        return sdf.format(retTime);
    }
}
