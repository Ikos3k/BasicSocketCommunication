package me.ANONIMUS.server.streams;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class NetOutput extends OutputStream {
    private final OutputStream stream;
    private final byte[] writeBuffer;

    public NetOutput(final OutputStream stream) {
        this.writeBuffer = new byte[8];
        this.stream = stream;
    }

    public void writeBytes(final byte[] b) throws IOException {
        this.stream.write(b);
    }

    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        this.stream.write(b, off, len);
    }

    @Override
    public void write(final int b) throws IOException {
        this.stream.write(b);
    }

    public void writeLong(final long l) throws IOException {
        this.writeBuffer[0] = (byte)(l >>> 56);
        this.writeBuffer[1] = (byte)(l >>> 48);
        this.writeBuffer[2] = (byte)(l >>> 40);
        this.writeBuffer[3] = (byte)(l >>> 32);
        this.writeBuffer[4] = (byte)(l >>> 24);
        this.writeBuffer[5] = (byte)(l >>> 16);
        this.writeBuffer[6] = (byte)(l >>> 8);
        this.writeBuffer[7] = (byte)(l);
        this.write(this.writeBuffer, 0, 8);
    }

    public void writeDouble(final double d) throws IOException {
        this.writeLong(Double.doubleToLongBits(d));
    }

    public void writeBoolean(final boolean b) throws IOException {
        this.stream.write(b ? 1 : 0);
    }

    public void writeInt(final int i) throws IOException {
        this.write(i >>> 24 & 0xFF);
        this.write(i >>> 16 & 0xFF);
        this.write(i >>> 8 & 0xFF);
        this.write(i & 0xFF);
    }

    public void writeFloat(final float f) throws IOException {
        this.writeInt(Float.floatToIntBits(f));
    }

    @Override
    public void flush() throws IOException {
        this.stream.flush();
    }

    @Override
    public void close() throws IOException {
        this.stream.close();
    }

    public void writeVarInt(int i) throws IOException {
        while ((i & 0xFFFFFF80) != 0x0) {
            this.write((i & 0x7F) | 0x80);
            i >>>= 7;
        }
        this.write(i);
    }

    public void writeString(final String s) throws IOException {
        final byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        if (bytes.length > 32767) {
            throw new RuntimeException("String too big (was " + bytes.length + " bytes encoded, max 32767)");
        }
        this.writeVarInt(bytes.length);
        this.write(bytes);
    }

    public void writeUnsignedShort(final int s) throws IOException {
        this.write(s >>> 8 & 0xFF);
        this.write(s & 0xFF);
    }

    public void writeShort(final short s) throws IOException {
        this.write(s >>> 8 & 0xFF);
        this.write(s & 0xFF);
    }

    public OutputStream getStream() {
        return this.stream;
    }
}