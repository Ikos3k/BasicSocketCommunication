package me.ANONIMUS.server.packet.impl.server;

import me.ANONIMUS.server.packet.Packet;
import me.ANONIMUS.server.streams.NetInput;
import me.ANONIMUS.server.streams.NetOutput;

public class ServerDisconnectPacket extends Packet {

    private String reason;

    public ServerDisconnectPacket() {}

    public ServerDisconnectPacket(String reason) {
        this.reason = reason;
    }

    @Override
    public void write(NetOutput out) throws Exception {
        out.writeString(reason);
    }

    @Override
    public void read(NetInput in) throws Exception {
        this.reason = in.readString(32767);
    }

    @Override
    public int getPacketID() {
        return 0x03;
    }

    public String getReason() {
        return reason;
    }
}
