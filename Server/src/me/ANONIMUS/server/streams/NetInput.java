package me.ANONIMUS.server.streams;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class NetInput extends InputStream {
    private final InputStream stream;
    private int limit;

    public NetInput(final InputStream stream) {
        this.stream = stream;
        this.limit = -1;
    }

    @Override
    public int read() throws IOException {
        if (this.limit == 0) {
            return -1;
        }
        if (this.limit > 0) {
            --this.limit;
        }
        final int str = this.stream.read();
        if (str == -1) {
            throw new EOFException();
        }
        return str;
    }

    @Override
    public int read(final byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }

    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        if (this.limit == 0) {
            return -1;
        }
        if (this.limit > 0) {
            final int readed = this.stream.read(b, off, Math.min(len, this.limit));
            this.limit -= readed;
            return readed;
        }
        return this.stream.read(b, off, len);
    }

    public byte readByte() throws IOException {
        return (byte)this.read();
    }

    public byte[] readBytes(int len) throws IOException {
        final byte[] b = new byte[len];
        while (len > 0) {
            final int n = this.read(b, b.length - len, len);
            if (n < 1) {
                throw new EOFException();
            }
            len -= n;
        }
        return b;
    }

    public boolean readBoolean() throws IOException {
        return readByte() == 1;
    }

    public int readInt() throws IOException {
        final int ch1 = this.read();
        final int ch2 = this.read();
        final int ch3 = this.read();
        final int ch4 = this.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0) {
            throw new EOFException();
        }
        return (ch1 << 24) + (ch2 << 16) + (ch3 << 8) + ch4;
    }

    public float readFloat() throws IOException {
        return Float.intBitsToFloat(this.readInt());
    }

    public long readLong() throws IOException {
        final byte[] readBuffer = this.readBytes(8);
        return (readBuffer[0] << 56) + ((readBuffer[1] & 0xFF) << 48) + ((readBuffer[2] & 0xFF) << 40) + ((readBuffer[3] & 0xFF) << 32) + ((readBuffer[4] & 0xFF) << 24) + ((readBuffer[5] & 0xFF) << 16) + ((readBuffer[6] & 0xFF) << 8) + ((readBuffer[7] & 0xFF) << 0);
    }

    public double readDouble() throws IOException {
        return Double.longBitsToDouble(this.readLong());
    }

    public int readVarInt() throws IOException {
        int var = 0;
        int moves = 0;
        byte buff;
        do {
            buff = this.readByte();
            var |= (buff & 0x7F) << moves++ * 7;
            if (moves > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((buff & 0x80) == 0x80);
        return var;
    }

    public String readString(final int maxLen) throws IOException {
        final int len = this.readVarInt();
        if (len > maxLen * 4) {
            throw new IOException("The received encoded string buffer length is longer than maximum allowed (" + len + " > " + maxLen * 4 + ")");
        }
        if (len < 0) {
            throw new IOException("The received encoded string buffer length is less than zero! Weird string!");
        }
        final String str = new String(this.readBytes(len), StandardCharsets.UTF_8);
        if (str.length() > maxLen) {
            throw new IOException("The received string length is longer than maximum allowed (" + len + " > " + maxLen + ")");
        }
        return str;
    }

    public int readUnsignedShort() throws IOException {
        final int ch1 = this.read();
        final int ch2 = this.read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (ch1 << 8) + ch2;
    }

    public short readShort() throws IOException {
        final int ch1 = this.read();
        final int ch2 = this.read();
        if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }
        return (short)((ch1 << 8) + ch2);
    }

    public InputStream getStream() {
        return this.stream;
    }

    public void setLimit(final int i) {
        this.limit = i;
    }

    public int getLimit() {
        return this.limit;
    }
}
