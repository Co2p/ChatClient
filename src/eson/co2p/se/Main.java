package eson.co2p.se;

import java.util.ArrayList;

/**
 * The Main class
 * @author Gordon, Isidor, Tony 24 October 2014
 */
public class Main {

    Thread nameServer;
    ArrayList<Thread> threads = new ArrayList<Thread>();
    startGui gui;
    ClientThread newServer;

    public Main(){
        askForNameServer nameServerQ = new askForNameServer();
        nameServerQ.CreatNameserverQuestion();
        startServer();
    }


    public static void main(String[] args) {

        Main newMain = new Main();

    }

    private void startServer(){
        nameServer = new Thread(new Runnable() {
            @Override
            public void run() {
                newServer = new ClientThread();
            }
        });
        threads.add(nameServer);
        nameServer.start();
    }
}
