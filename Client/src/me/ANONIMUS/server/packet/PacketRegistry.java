package me.ANONIMUS.server.packet;

import me.ANONIMUS.server.packet.impl.client.ClientChatPacket;
import me.ANONIMUS.server.packet.impl.client.ClientLoginStartPacket;
import me.ANONIMUS.server.packet.impl.client.ClientTestPacket;
import me.ANONIMUS.server.packet.impl.server.ServerChatPacket;
import me.ANONIMUS.server.packet.impl.server.ServerDisconnectPacket;
import me.ANONIMUS.server.packet.impl.server.ServerLoginDisconnectPacket;
import me.ANONIMUS.server.packet.impl.server.ServerLoginSuccessPacket;

import java.util.HashMap;

public class PacketRegistry {
    private static final HashMap<Integer, Packet> TO_CLIENT = new HashMap<>();
    private static final HashMap<Integer, Packet> TO_SERVER = new HashMap<>();

    public static void registerPacket(PacketDirection direction, Packet packet) {
        switch(direction){
            case SERVERBOUND:
                TO_SERVER.put(packet.getPacketID(), packet);
                break;
            case CLIENTBOUND:
                TO_CLIENT.put(packet.getPacketID(), packet);
                break;
        }
    }

    public static void init() {
        registerPacket(PacketDirection.SERVERBOUND, new ClientTestPacket());
        registerPacket(PacketDirection.SERVERBOUND, new ClientLoginStartPacket());
        registerPacket(PacketDirection.SERVERBOUND, new ClientChatPacket());

        registerPacket(PacketDirection.CLIENTBOUND, new ServerLoginDisconnectPacket());
        registerPacket(PacketDirection.CLIENTBOUND, new ServerLoginSuccessPacket());
        registerPacket(PacketDirection.CLIENTBOUND, new ServerDisconnectPacket());
        registerPacket(PacketDirection.CLIENTBOUND, new ServerChatPacket());
    }

    public static Packet getPacket(PacketDirection direction, int id) {
        switch(direction){
            case SERVERBOUND:
                return TO_SERVER.get(id);
            case CLIENTBOUND:
                return TO_CLIENT.get(id);
        }
        return null;
    }
}