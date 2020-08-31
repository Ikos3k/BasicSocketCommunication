package me.ANONIMUS.server.threads;

public class MemoryFreeThread extends Thread {
    @Override
    public void run() {
        while(true) {
            System.out.println("Freeing memory...");
            System.gc();
            try {
                Thread.sleep(60000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}