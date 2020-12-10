package chess.protocol;

import java.nio.ByteBuffer;

public class Header {
    public class Command {
        public static final byte NONE = 0;

        public static final byte CONNECT = 10; // Channel CONTROL: authentication request sent by clients
        public static final byte DISCONNECT = 11; // Channel CONTROL: sent by either parties before closing the control
                                                  // channel

        public static final byte GET = 20; // Channel DATA: sent by clients to request a piece of data
        public static final byte POST = 21; // Channel DATA: sent by clients to trigger an action on the server
        public static final byte NOTIFY = 21; // Channel DATA: sent by the server to update the state of a client

        public static final byte OK = 30; // Channel CONTROL/DATA: generic success response
        public static final byte KO = 31; // Channel CONTROL/DATA: generic failure response
        public static final byte KEEPALIVE = 32; // Channel CONTROL/DATA: periodically sent by clients to maintain the
                                                 // connection in
    }

    private byte command = Command.NONE;
    private int payloadLength = 0;

    Header() {
        
    }

    public byte getCommand() {
        return this.command;
    }

    public void setCommand(byte command) {
        this.command = command;
    }

    public int getPayloadLength() {
        return this.payloadLength;
    }

    public void setPayloadLength(int inBytes) {
        this.payloadLength = inBytes;
    }

    // ----- STATIC FIELDS ----- //
    public static final int SIZE_ENCODED = Byte.BYTES + Integer.BYTES;

    public static Header decode(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);

        Header header = new Header();
        header.setCommand(buffer.get());
        header.setPayloadLength(buffer.getInt());
        return header;
    }

    public static byte[] encode(Header data) {
        byte[] bytes = new byte[SIZE_ENCODED];

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.put(data.getCommand());
        buffer.putInt(data.getPayloadLength());

        return bytes;
    }
}
