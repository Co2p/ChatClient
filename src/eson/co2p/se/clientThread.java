package eson.co2p.se;

import java.util.ArrayList;

/**
 * Created by Tony on 07/10/2014.
 *
 * Creates a TCP server and forwards messages to it.
 */
public class clientThread {

    private startGui gui;
    private Thread senderThread;
    private senderServer sender;
    private ArrayList<Thread> threads = new ArrayList<Thread>();

    //skapa konstruktor
    public clientThread(startGui gui) {

        private void startSender () {
            senderThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    sender = new senderServer();
                }
            });
            threads.add(senderThread);
            senderThread.start();
        }

    }
}
