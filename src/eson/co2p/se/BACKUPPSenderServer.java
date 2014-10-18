package eson.co2p.se;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by gordon on 08/10/14.
 *
 * TCP connection to the Chat Server
 */
public class BACKUPPSenderServer {
    private ServerSocket localServerSocket;
    private Socket tagetSocket;
    private PDU pdu;
    private byte[] data;
    private BufferedReader in;
    private PrintStream out;

    public BACKUPPSenderServer() {

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
