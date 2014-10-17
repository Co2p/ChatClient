package eson.co2p.se;

import java.util.ArrayList;

/**
 * Created by Tony on 07/10/2014.
 *
 * Creates a TCP server and forwards messages to it.
 */
public class ClientThread {

    private startGui userGui;
    private Thread senderThread;
    private SenderServer sender;
    private ArrayList<Thread> threads = new ArrayList<Thread>();

    //skapa konstruktor
    public ClientThread(startGui gui) {
        userGui = gui;
        testFeedGui();
    }

    public ClientThread() {

    }

    //Skriv till GUI TODO: segmentera medelanden
    private void printToGui(String printString){
        //userGui.outputWindow(printString + "\n");
    }

    private void requestServers(){

    }

    /**
     * Starts the TCP connection thread that connects to the server
     */
    private void startSender(){
        senderThread = new Thread(new Runnable(){
            @Override
            public void run(){
                sender = new SenderServer();
            }
        });
        threads.add(senderThread);
        senderThread.start();
    }

    //Testfunktion till GUI feed
    public void testFeedGui(){
        int x = 20;
        while(x >= 0){
            if(x < 1){
                printToGui("lol: " + x + "times upp");
            }
            else{
                printToGui("lol: " + x);
            }
            x--;
        }
    }
}
