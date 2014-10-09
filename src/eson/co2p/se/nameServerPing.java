package eson.co2p.se;

import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by gordon on 07/10/14.
 *
 */
public class nameServerPing {

    private String ip, port;
    private int localPort = 1337;
    private ServerSocket serverSocket;
    private ArrayList<Integer> format;
    private ArrayList<String> content;
    private ArrayList<Byte> byteBuffer;

    /**
     * Constructor
     * @param ip Server IP
     * @param port Server Port
     */
    public nameServerPing(String ip, String port){
        this.ip=ip;
        this.port=port;

        serverPing();
    }

    /**
     * Sends a UDP request to the nameserver
     */
    public void serverPing(){
        try {
            serverSocket = new ServerSocket(localPort);
            Socket socket = serverSocket.accept();

                DataOutputStream out;
                try {
                    out = new DataOutputStream(serverSocket.accept().getOutputStream());
                    out.write(byteConverter.headerBuilder(format, content).toByteArray());
                } catch (IOException e) {
                    System.out.println("Error while sending request to nameserver");
                    e.printStackTrace();
                }

            BufferedReader listener = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Byte tempByte = (byte) listener.read();
            byteBuffer.add(tempByte);

            System.out.println(byteBuffer.get(0));

        } catch (IOException e) {
            System.out.println("Error while creating a port for nameserver");
            e.printStackTrace();
        }



    }
}
