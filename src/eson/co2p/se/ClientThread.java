package eson.co2p.se;

import java.util.ArrayList;

/**
 * Created by Tony on 07/10/2014.
 *
 * Creates a TCP server and forwards messages to it.
 */
public class ClientThread {
    static Thread connectCurentServer;
    static SenderServer newServer;
    static ArrayList<ArrayList> ServerThreadList = new ArrayList<ArrayList>();
    static int[] AliveThreadsID = new int[256];

    public static void startThread(final int ThreadId, final ServerList Server, final ArrayList serverlist) {
        ArrayList<Object> Templist = new ArrayList<Object>();
        connectCurentServer = new Thread(new Runnable() {
            @Override
            public void run() {
                newServer = new SenderServer(Server.getServer((String) serverlist.get(ThreadId)).getIp(), Server.getServer((String) serverlist.get(ThreadId)).getPort(), ThreadId);
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
    public static void endThread(final int ThreadId){
        int ID = ThreadId;
        for(ArrayList Me : ServerThreadList){
            Object Targ = Me.get(0);
            int ObjId = (Integer)Targ;
            if (ObjId == ID){
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
