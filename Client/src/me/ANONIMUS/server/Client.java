package me.ANONIMUS.server;

import me.ANONIMUS.server.packet.PacketRegistry;
import me.ANONIMUS.server.threads.ClientConnection;

import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args)  {
        System.out.println("Registry packets...");
        PacketRegistry.init();
        try {
            Socket socket = new Socket("127.0.0.1", 1337);

            new ClientConnection(socket).start();
        } catch (IOException e) {
            System.err.println("[ERROR] Server is not responding!");
        }
    }
}