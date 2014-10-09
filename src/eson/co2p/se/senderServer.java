package eson.co2p.se;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by gordon on 08/10/14.
 *
 * TCP connection to the Chat Server
 */
public class senderServer {

    private ServerSocket localServerSocket;
    private BufferedReader byteArrayInput;
    private char[] charBuff = new char[65535];

    public senderServer() {

        try {
            localServerSocket = new ServerSocket(1339);


            try {
                Socket localSocket = null;
                localSocket = localServerSocket.accept();


                try {
                    byteArrayInput = new BufferedReader(new InputStreamReader(localSocket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String listen(){

        try {
            byteArrayInput.read(charBuff);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(charBuff);

        return charBuff.toString();
    }
}
