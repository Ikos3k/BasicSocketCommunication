package me.ANONIMUS.server.packet.impl.server;

import me.ANONIMUS.server.packet.Packet;
import me.ANONIMUS.server.streams.NetInput;
import me.ANONIMUS.server.streams.NetOutput;

public class ServerChatPacket extends Packet {
    private String message;

    public ServerChatPacket() {}

    public ServerChatPacket(String message) {
        this.message = message;
    }

    @Override
    public void write(NetOutput out) throws Exception {
        out.writeString(message);
    }

    @Override
    public void read(NetInput in) throws Exception {
        this.message = in.readString(32767);
    }

    @Override
    public int getPacketID() {
        return 0x02;
    }

    public String getMessage() {
        return message;
    }
}
