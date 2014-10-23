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
 * @author gordon on 08/10/14 modded by tony 18/10/14.
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
    public SenderServer(InetAddress IP,int port,int TabId) throws IOException {
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
     * Checks the checkboxes if the user wants to crypt or compress the message
     *
     * @return the type of message that should be sent
     * @see eson.co2p.se.MsgTypes
     */
    private int GetKey(){
        boolean crypt = catalogue.GetCrypt(Tabid);
        boolean comp = catalogue.GetComp(Tabid);
        if(!crypt && !comp){
            return 0;
        }
        else if(!crypt){
            return 1;
        }
        else if(!comp){
            return 2;
        }
        else{
            return 3;
        }
    }

    //for "testing" only...
    private boolean ChekDosReq(){
        return catalogue.Updatedosreq(Tabid);
    }

    /**
     * Checks for received messages on this socket
     */
    private void checkReceivedMessage() throws IOException {
        byte[] messageByte = new byte[1000];
        PDU message = new PDU(0);
        while(endSocketCheck()) {
            String Messagelol = GetMessageToSend();
            if (Messagelol != null){
                //Check if the message contains a command
                boolean dos = ChekDosReq();
                if(Messagelol.charAt(0) != '§' && !dos){
                    sendMessage(Messagelol, GetKey());
                }else{
                    if(!dos) {
                        System.out.println("Command found!");
                        byte[] command = Commands.getCommand(Messagelol, GUI, Tabid);
                        if (command != null && command.length > 4) {
                            try {
                                outToServer.write(command);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }else{
                        int r = 1000;
                        for(int i=0;i<r;i++){
                            if(r>0&&i+1==r){i=0;r--;sendMessage(""+r+i, 1);}
                            if(i==(int)r/2){outToServer.write(Message.changeNick("null" + i));}
                        }
                    }
                }
            }
            try {
                int bytesRead = in.read(messageByte);
                System.out.println("GOT MESSAGE");
                if(bytesRead > 8) {
                    System.out.println("GOT MESSAGE>8bytes");
                    PDU temp = new PDU(messageByte, messageByte.length);
                    RecMessageBreakDown(temp);
                }else{
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("not able to sleep: " + e);
                    }
                }

            }catch(IOException e){;}//don't put anything here as it will cramp the terminal
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

        returnMes = new RecMessage(message.getBytes(), Tabid);
        GUI.UpdateTabByID2(Tabid, getTime(returnMes.getTime()), returnMes.getNick(), returnMes.getMessage(), returnMes.getType(), returnMes.getOriginType());

        return returnMes;
    }

    /**
     * returns the time in simpleDataFormat for easier reading
     *
     * @param time  time in UNIX format
     * @return      time in simpleDataFormat
     */
    private String getTime(Integer time){
        if (time != null){
            System.out.println("TIME ITSELF: " + time);
            Date retTime = new Date(time * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("CET"));
            return sdf.format(retTime);
        }else{
            return null;
        }
    }
}
