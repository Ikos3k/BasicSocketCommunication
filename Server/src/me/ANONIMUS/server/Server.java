package me.ANONIMUS.server;

import me.ANONIMUS.server.packet.Packet;
import me.ANONIMUS.server.packet.PacketRegistry;
import me.ANONIMUS.server.threads.MemoryFreeThread;
import me.ANONIMUS.server.threads.ServerConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private static List<ServerConnection> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        System.out.println("Registry packets...");
        PacketRegistry.init();

        new MemoryFreeThread().start();

        ServerSocket serverSocket = new ServerSocket(1337);

        while (true) {
            System.out.println("Waiting for client connection...");
            Socket client = serverSocket.accept();

            ServerConnection serverConnection = new ServerConnection(client);
            serverConnection.start();
            clients.add(serverConnection);
        }
    }

    public static void sendPacketToAll(Packet packet) {
        for(ServerConnection connection : clients) {
            try {
                connection.getOut().writeInt(packet.getPacketID());
                packet.write( connection.getOut());
                connection.getOut().flush();
            } catch (Exception ignored) { }
        }
    }

    public static List<ServerConnection> getClients() {
        return clients;
    }
}