package eson.co2p.se;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Thread nameServer;
        ArrayList<Thread> threads = new ArrayList<Thread>();



        nameServer = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        threads.add(nameServer);
        nameServer.start();



    }
}
