package eson.co2p.se;

import java.util.ArrayList;

public class Main {

    Thread nameServer;
    ArrayList<Thread> threads = new ArrayList<Thread>();
    startGui gui;
    clientThread newServer;

    public Main(){
        askForNameServer nameServerQ = new askForNameServer();
        nameServerQ.CreatNameserverQuestion();
        //gui = new startGui();
        startServer();
    }


    public static void main(String[] args) {

        //creates a new instanse of this function, so that the constructor can run.
        Main newMain = new Main();

    }

    private void startServer(){
        nameServer = new Thread(new Runnable() {
            @Override
            public void run() {
                newServer = new clientThread();
            }
        });
        threads.add(nameServer);
        nameServer.start();
    }
}
