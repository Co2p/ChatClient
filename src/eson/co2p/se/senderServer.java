package eson.co2p.se;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by gordon on 08/10/14.
 *
 * TCP connection to the Chat Server
 */
public class senderServer {

    private ServerSocket localServerSocket;
    private Socket tagetSocket;
    private PDU pdu;
    private byte[] data;
    private BufferedReader in;
    private PrintStream out;

    public senderServer() {

        try {
            tagetSocket = new Socket(catalogue.getNameServerInet(), catalogue.getNameServerPort());
            out = new PrintStream(tagetSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(tagetSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            out.write(pdu.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String messageS){
        try {
            out.write(message.sendMessage(messageS, catalogue.getMessageType()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
