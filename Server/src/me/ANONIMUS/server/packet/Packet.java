package me.ANONIMUS.server.packet;

import lombok.Data;
import me.ANONIMUS.server.streams.NetInput;
import me.ANONIMUS.server.streams.NetOutput;

@Data
public abstract class Packet {
    public abstract void write(NetOutput out) throws Exception;
    public abstract void read(NetInput in) throws Exception;
    public abstract int getPacketID();
}