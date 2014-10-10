package eson.co2p.se;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by gordon on 08/10/14.
 * Remembers states and identifiers
 */
public class catalogue {

    private static int idNumber;
    private static String name, serverIP, serverPort;

    private catalogue(){}

    /**
     * Sets the ID number that the client received from the server
     * @param idNumber the ID number
     */
    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    /**
     * Sets the user name
     * @param name user name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Saves the server IP address
     * @param serverIP the IP as a String
     */
    public void setServerIP(String serverIP) {
        try {
            InetAddress inetAddress = InetAddress.getByName(serverIP);
            this.serverIP = inetAddress.toString();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the server port number
     * @param serverPort portnumber as a String
     */
    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * Returns the client ID number
     * @return ID number
     */
    public int getIdNumber() {
        return idNumber;
    }

    /**
     * Returns the user name
     * @return user name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the server IP address
     * @return server IP
     */
    public String getServerIP() {
        return serverIP;
    }

    /**
     * Returns the server port
     * @return server port
     */
    public String getServerPort() {
        return serverPort;
    }
}
