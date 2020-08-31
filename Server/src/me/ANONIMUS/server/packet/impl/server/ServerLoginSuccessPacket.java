package me.ANONIMUS.server.packet.impl.server;

import me.ANONIMUS.server.packet.Packet;
import me.ANONIMUS.server.streams.NetInput;
import me.ANONIMUS.server.streams.NetOutput;

public class ServerLoginSuccessPacket extends Packet {
    private String text;

    public ServerLoginSuccessPacket() {}

    public ServerLoginSuccessPacket(String text) {
        this.text = text;
    }

    @Override
    public void write(NetOutput out) throws Exception {
        out.writeString(text);
    }

    @Override
    public void read(NetInput in) throws Exception {
        this.text = in.readString(16);
    }

    @Override
    public int getPacketID() {
        return 0x01;
    }

    public String getText() {
        return text;
    }
}