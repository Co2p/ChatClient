package eson.co2p.se;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Creates a TCP server and forwards messages to it.
 * @author Gordon, Isidor, Tony 23 October 2014
 *
 */
public class ClientThread {

    static Thread connectCurentServer;
    static SenderServer newServer;
    static ArrayList<ArrayList> ServerThreadList = new ArrayList<ArrayList>();
    static int[] AliveThreadsID = new int[256];

    /**
     * Is called to start a new connection
     * @param ThreadId a thread identifier
     * @param Server defines which server to connect to
     *               @see eson.co2p.se.ServerList
     *               @see eson.co2p.se.Server
     * @param serverlist a list of active servers in the gui
     */
    public static void startThread(final int ThreadId, final ServerList Server, final ArrayList serverlist) {
        ArrayList<Object> Templist = new ArrayList<Object>();
        connectCurentServer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    newServer = new SenderServer(Server.getServer((String) serverlist.get(ThreadId)).getIp(), Server.getServer((String) serverlist.get(ThreadId)).getPort(), ThreadId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //SenderServer MyServer = new SenderServer(Server.getServer((String) serverlist.get(getSelectedServerTab())).getIp(), Server.getServer((String) serverlist.get(getSelectedServerTab())).getPort(), getSelectedServerTab());
        Templist.add(ThreadId);
        Templist.add(connectCurentServer);
        Templist.add(newServer);
        ServerThreadList.add(Templist);
        connectCurentServer.start();
        AliveThreadsID[ThreadId] = 1; //1 = alive, 0 = dead
        System.out.println("Started server thread whit ID:" + ThreadId);
    }
    public static void startThreadManualy(final int ThreadId, final String Server, final int serverlist) {
        ArrayList<Object> Templist = new ArrayList<Object>();
        connectCurentServer = new Thread(new Runnable() {
            @Override
            public void run() {
                InetAddress adress = null;
                try {
                    adress = InetAddress.getByName(Server);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                try {
                    newServer = new SenderServer(adress, serverlist, ThreadId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //SenderServer MyServer = new SenderServer(Server.getServer((String) serverlist.get(getSelectedServerTab())).getIp(), Server.getServer((String) serverlist.get(getSelectedServerTab())).getPort(), getSelectedServerTab());
        Templist.add(ThreadId);
        Templist.add(connectCurentServer);
        Templist.add(newServer);
        ServerThreadList.add(Templist);
        connectCurentServer.start();
        AliveThreadsID[ThreadId] = 1; //1 = alive, 0 = dead
        System.out.println("Started server thread whit ID:" + ThreadId);
    }
    /**
     * Stops a thread with the given id number
     * @param ThreadId a thread identifier
     */
    public static void endThread(final int ThreadId){
        for(ArrayList Me : ServerThreadList){
            Object Targ = Me.get(0);
            int ObjId = (Integer)Targ;
            if (ObjId == ThreadId){
                System.out.println("ME:" + Me );
                AliveThreadsID[ObjId] = 0;
                Thread Threaded = (Thread)Me.get(1);//the thread
                System.out.println("TH:" + Threaded );
                System.out.println("TH:" + Threaded.getName() );
                SenderServer cServer = (SenderServer)Me.get(2);
                //cServer.endSocket();
                System.out.println("closing server whit id:" + ObjId );
                //remove old thread from list?
            }
        }
    }

}
