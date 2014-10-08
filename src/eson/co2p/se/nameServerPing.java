package eson.co2p.se;

import com.sun.net.httpserver.HttpServer;

/**
 * Created by gordon on 07/10/14.
 *
 * Pings the name server, makes sure that the chat server is still online
 * Sends the message as a bytestream containing the following:
 * byte 0: "OP: ALIVE" (ALIVE = 2 in utf-8) byte 1: "Number of clients" byte 2-3: "ID of client"
 */
public class nameServerPing {

}
