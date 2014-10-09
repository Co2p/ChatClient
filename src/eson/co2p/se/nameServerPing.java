package eson.co2p.se;

import com.sun.net.httpserver.HttpServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
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
        } catch (IOException e) {
            System.out.println("Error while creating a port for nameserver");
            e.printStackTrace();
        }

        DataOutputStream out;
        try {
            out = new DataOutputStream(serverSocket.accept().getOutputStream());
            out.write(byteConverter.headerBuilder(format, content).toByteArray());
        } catch (IOException e) {
            System.out.println("Error while sending request to nameserver");
            e.printStackTrace();
        }

    }
}
