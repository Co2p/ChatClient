package eson.co2p.se;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by gordon on 08/10/14.
 *
 * TCP connection to the Chat Server
 */
public class SenderServer {
    private ServerSocket localServerSocket;
    private Socket tagetSocket;
    private PDU pdu;
    private byte[] data;
    private BufferedReader in;
    private PrintStream out;

    public SenderServer() {
        try {
            tagetSocket = new Socket(catalogue.getNameServerInet(), catalogue.getNameServerPort());
            out = new PrintStream(tagetSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(tagetSocket.getInputStream()));
            out.write(pdu.getBytes());
            in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String messageS){
        try {
            out.write(Message.sendMessage(messageS, catalogue.getMessageType()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
