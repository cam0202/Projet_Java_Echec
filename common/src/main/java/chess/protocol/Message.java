package chess.protocol;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Implements our protocol, and provide useful functions to abstract it away.
 */
public class Message {
    public static class Type {
        public static final int DISCOVER = 100;

        public static final int OK = 200;

        public static final int CONNECT = 300;
        public static final int DISCONNECT = 301;
        
        public static final int GET = 310;
        public static final int POST = 311;
        
        public static final int PUSH = 320;

        public static final int KO = 400;
    }

    public static final int HEADER_SIZE = Integer.BYTES + Integer.BYTES;
    public static final int MAX_LENGTH = 4096;

    private final int type;
    private int length;
    private byte[] data;

    public Message(int type) {
        this.type = type;
        this.length = 0;
        this.data = new byte[0];
    }

    public Message(final byte[] header) {
        if (header.length < HEADER_SIZE) {
            throw new IllegalArgumentException("too few bytes for header");
        }

        ByteBuffer buffer = ByteBuffer.wrap(header);
        this.type = buffer.getInt();
        this.length = buffer.getInt();

        if (this.length > MAX_LENGTH - HEADER_SIZE) {
            throw new IllegalArgumentException("ill-formed header: payload too long");
        }

        this.data = new byte[0];
    }

    public void setData(final String data) {
        this.setDataBytes(data.getBytes(StandardCharsets.UTF_8));
    }

    public void setDataBytes(final byte[] data) {
        if (data.length > MAX_LENGTH - HEADER_SIZE) {
            throw new IllegalArgumentException("message too long");
        }

        this.data = data;
        this.length = this.data.length;
    }

    public int getType() {
        return this.type;
    }

    public String getData() {
        return new String(this.getDataBytes(), StandardCharsets.UTF_8);
    }

    public byte[] getDataBytes() {
        return this.data;
    }

    public int getAmountOfMissingDataBytes() {
        return this.length - this.data.length;
    }

    public byte[] getBytes() {
        if (this.getAmountOfMissingDataBytes() != 0) {
            throw new IllegalStateException("payload is incomplete");
        }

        byte[] bytes = new byte[HEADER_SIZE + data.length];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.putInt(this.type);
        buffer.putInt(this.length);
        buffer.put(this.data);
        return bytes;
    }
}
