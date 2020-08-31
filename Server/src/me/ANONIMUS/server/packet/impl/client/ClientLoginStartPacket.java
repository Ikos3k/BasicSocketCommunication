package me.ANONIMUS.server.packet.impl.client;

import me.ANONIMUS.server.packet.Packet;
import me.ANONIMUS.server.streams.NetInput;
import me.ANONIMUS.server.streams.NetOutput;

public class ClientLoginStartPacket extends Packet {
    private String username;

    public ClientLoginStartPacket() {}

    public ClientLoginStartPacket(String username) {
        this.username = username;
    }

    @Override
    public void write(NetOutput out) throws Exception {
        out.writeString(username);
    }

    @Override
    public void read(NetInput in) throws Exception {
        this.username = in.readString(16);
    }

    @Override
    public int getPacketID() {
        return 0x00;
    }

    public String getUsername() {
        return username;
    }
}
