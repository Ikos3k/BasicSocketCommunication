package me.ANONIMUS.server.threads;

import me.ANONIMUS.server.packet.Packet;
import me.ANONIMUS.server.packet.PacketDirection;
import me.ANONIMUS.server.packet.PacketRegistry;
import me.ANONIMUS.server.packet.impl.client.ClientChatPacket;
import me.ANONIMUS.server.packet.impl.client.ClientLoginStartPacket;
import me.ANONIMUS.server.packet.impl.client.ClientTestPacket;
import me.ANONIMUS.server.packet.impl.server.ServerChatPacket;
import me.ANONIMUS.server.packet.impl.server.ServerDisconnectPacket;
import me.ANONIMUS.server.packet.impl.server.ServerLoginDisconnectPacket;
import me.ANONIMUS.server.packet.impl.server.ServerLoginSuccessPacket;
import me.ANONIMUS.server.streams.NetInput;
import me.ANONIMUS.server.streams.NetOutput;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;

public class ClientConnection extends Thread {
    private NetInput in;
    private NetOutput out;

    private boolean connected;

    public ClientConnection(Socket client) throws IOException {
        this.in = new NetInput(client.getInputStream());
        this.out = new NetOutput(client.getOutputStream());
        this.connected = true;
    }

    @Override
    public void run() {
        sendPacket(new ClientLoginStartPacket("Ikosek"), out);
        while(connected) {
            try {
                Packet p = PacketRegistry.getPacket(PacketDirection.CLIENTBOUND, in.readInt());
                p.read(in);
                System.out.println("[READ] packet: [" + p.getPacketID() + "] " + p.getClass().getSimpleName());
                if (p instanceof ServerLoginSuccessPacket) {
                    System.out.println("ServerLoginSuccessPacket > " + ((ServerLoginSuccessPacket) p).getText());
                    sendPacket(new ClientChatPacket("Hi"), out);
                    sendPacket(new ClientTestPacket(new byte[5], 8, ";D", true, Collections.singletonList("test1, test2")), out);
                }
                if (p instanceof ServerChatPacket) {
                    System.out.println("[SERVER] message: " + ((ServerChatPacket) p).getMessage());
                }
                if (p instanceof ServerLoginDisconnectPacket) {
                    this.connected = false;
                    System.out.println("Disconnected during login! reason: " + ((ServerLoginDisconnectPacket) p).getReason());
                }
                if (p instanceof ServerDisconnectPacket) {
                    this.connected = false;
                    System.out.println("Disconnected! reason: " + ((ServerDisconnectPacket) p).getReason());
                }
            } catch (Exception e) {
                this.connected = false;
            }
        }
    }

    public void sendPacket(Packet p, NetOutput printStream) {
        try {
            printStream.writeInt(p.getPacketID());
            p.write(printStream);
            printStream.flush();
        } catch (Exception ignored) { }
    }
}
