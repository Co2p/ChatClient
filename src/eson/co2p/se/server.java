package eson.co2p.se;

import java.net.InetAddress;

/**
 * Created by isidor on 2014-10-13.
 */
public class server {
    InetAddress ip;
    String name;
    int port;
    int connected;

    public server(){
    }

    public void setIp(InetAddress ip){
        this.ip = ip;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPort(int port){
        this.port = port;
    }

    public void setConnected(int connected){
        this.connected = connected;
    }

    public InetAddress getIp(){
        return this.ip;
    }
    public String getName(){
        return this.name;
    }

    public int getPort(){
        return this.port;
    }

    public int getConnected(){
        return this.connected;
    }
}
