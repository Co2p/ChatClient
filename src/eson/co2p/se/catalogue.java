package eson.co2p.se;

/**
 * Created by gordon on 08/10/14.
 * Remembers states and identifiers
 */
public final class catalogue {

    private int idNumber;
    private String name, serverIP, serverPort;

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
        this.serverIP = serverIP;
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
