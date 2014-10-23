package eson.co2p.se;

import java.net.InetAddress;

/**
 * An object to store information about servers
 *
 * @author Gordon, Isidor, Tony 23 October 2014
 */
public class Server {
    private InetAddress ip;
    private String name;
    private int port;
    private int connected;

    public Server(){
    }

    /**
     * Set the server ip
     * @param ip the ip
     */
    public void setIp(InetAddress ip){
        this.ip = ip;
    }

    /**
     * Set the server name
     * @param name server name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Set the server port
     * @param port port number
     */
    public void setPort(int port){
        this.port = port;
    }

    /**
     * Set how many clients are connected to the server
     * @param connected Active users
     */
    public void setConnected(int connected){
        this.connected = connected;
    }

    /**
     * Returns the server ip
     * @return server ip
     */
    public InetAddress getIp(){
        return this.ip;
    }

    /**
     * Get the server name
     * @return server name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Get the server port
     * @return server port
     */
    public int getPort(){
        return this.port;
    }

    /**
     * Get the number of connected clients to the server
     * @return connected clients
     */
    public int getConnected(){
        return this.connected;
    }
}
