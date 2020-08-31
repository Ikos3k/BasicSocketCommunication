package me.ANONIMUS.server.packet.impl.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.ANONIMUS.server.packet.Packet;
import me.ANONIMUS.server.streams.NetInput;
import me.ANONIMUS.server.streams.NetOutput;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientTestPacket extends Packet {
    private byte[] bytes;
    private int i;
    private String s;
    private boolean b;
    private List<String> list;

    @Override
    public void write(NetOutput out) throws Exception {
        out.writeInt(bytes.length);
        out.writeBytes(bytes);

        out.writeInt(i);

        out.writeString(s);

        out.writeBoolean(b);

        out.writeInt(list.size());
        for(String s : list) {
            out.writeString(s);
        }
    }

    @Override
    public void read(NetInput in) throws Exception {
        this.bytes = in.readBytes(in.readInt());

        this.i = in.readInt();

        this.s = in.readString(16);

        this.b = in.readBoolean();

        final int f = in.readInt();
        List<String> x = new ArrayList<>();
        for(int i = 0; i < f; i++) {
            x.add(in.readString(16));
        }
        this.list = x;
    }

    @Override
    public int getPacketID() {
        return 0x02;
    }
}