package me.ANONIMUS.server.threads;

import me.ANONIMUS.server.Server;
import me.ANONIMUS.server.packet.Packet;
import me.ANONIMUS.server.packet.PacketDirection;
import me.ANONIMUS.server.packet.PacketRegistry;
import me.ANONIMUS.server.packet.impl.client.ClientChatPacket;
import me.ANONIMUS.server.packet.impl.client.ClientLoginStartPacket;
import me.ANONIMUS.server.packet.impl.client.ClientTestPacket;
import me.ANONIMUS.server.packet.impl.server.ServerChatPacket;
import me.ANONIMUS.server.packet.impl.server.ServerLoginDisconnectPacket;
import me.ANONIMUS.server.packet.impl.server.ServerLoginSuccessPacket;
import me.ANONIMUS.server.streams.NetInput;
import me.ANONIMUS.server.streams.NetOutput;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;


public class ServerConnection extends Thread {
    private NetInput in;
    private NetOutput out;

    private boolean running;
    private String nickname;

    public ServerConnection(Socket client) throws IOException {
        this.in = new NetInput(client.getInputStream());
        this.out = new NetOutput(client.getOutputStream());
        this.running = true;
    }

    @Override
    public void run() {
        try {
            while (running) {
                Packet p = PacketRegistry.getPacket(PacketDirection.SERVERBOUND, in.readInt());
                p.read(in);

                System.out.println((nickname != null ? "[" + nickname + "] " : "") + "packet: [" + p.getPacketID() + "] " + p.getClass().getSimpleName());
                if(p instanceof ClientLoginStartPacket) {
                    final String name = ((ClientLoginStartPacket) p).getUsername();
                    if(name.isEmpty()) {
                        sendPacket(new ServerLoginDisconnectPacket("bad name :/"), out);
                        out.close();
                        in.close();
                    }
                    this.nickname = ((ClientLoginStartPacket) p).getUsername();
                    System.out.println(this.nickname + " conneted!");
                    Server.sendPacketToAll(new ServerChatPacket("Player " + name + " connected!"));
                    sendPacket(new ServerLoginSuccessPacket(":p"), out);
                }
                if(p instanceof ClientChatPacket) {
                    final String message = ((ClientChatPacket) p).getMessage();
                    System.out.println("[" + this.nickname + "] message: " + message);
                    if(message.equals("Hi")) {
                        sendPacket(new ServerChatPacket("Hi " + nickname + "!"), out);
                    }
                }
                if(p instanceof ClientTestPacket) {
                    System.out.println("bytes: " + Arrays.toString(((ClientTestPacket) p).getBytes()));
                    System.out.println("int: " + ((ClientTestPacket) p).getI());
                    System.out.println("string: " + ((ClientTestPacket) p).getS());
                    System.out.println("boolean: " + ((ClientTestPacket) p).isB());
                    System.out.println("list: " + ((ClientTestPacket) p).getList());
                }
            }
        } catch (Exception e) {
            running = false;
            System.out.println(nickname + " disconnected!");
            Server.getClients().remove(this);
        } finally {
            try {
                out.close();
            } catch (IOException ignored) { }
            try {
                in.close();
            } catch (IOException ignored) { }
        }
    }

    public void sendPacket(Packet p, NetOutput out) {
        try {
            out.writeInt(p.getPacketID());
            p.write(out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NetOutput getOut() {
        return out;
    }

    public String getNickname() {
        return nickname;
    }
}
