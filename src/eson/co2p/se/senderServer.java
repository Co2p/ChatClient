package eson.co2p.se;

import sun.jvm.hotspot.runtime.Bytes;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
    private catalogue catalogue;
    private BufferedReader in;
    private PrintStream out;
    private char[] charBuff = new char[65535];

    public senderServer() {

        try {
            tagetSocket = new Socket(catalogue.getServerIP(), Integer.parseInt(catalogue.getServerPort()));
            out = new PrintStream(tagetSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(tagetSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        data[1] = 12;

        pdu = new PDU(data, 32);
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


}
