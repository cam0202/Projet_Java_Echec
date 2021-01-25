package chess.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import chess.protocol.Message;

public class TCPExchange {

    public static void send(final Socket socketTCP, final ExchangePacket packet) throws IOException {
        validateSocket(socketTCP);

        if (packet == null) {
            throw new IllegalArgumentException("packet is null");
        }

        BufferedOutputStream out = new BufferedOutputStream(socketTCP.getOutputStream());
        out.write(packet.getMessage().getBytes());
        out.flush();
    }

    public static ExchangePacket receive(final Socket socketTCP) throws IOException {
        validateSocket(socketTCP);

        BufferedInputStream in = new BufferedInputStream(socketTCP.getInputStream());

        byte[] header = new byte[Message.HEADER_SIZE];
        fillBufferFromStream(in, header);

        ExchangePacket p = new ExchangePacket(socketTCP, new Message(header));

        byte[] payload = new byte[p.getMessage().getAmountOfMissingDataBytes()];
        if (payload.length > 0) {
            fillBufferFromStream(in, payload);
            p.getMessage().setDataBytes(payload);
        }

        return p;
    }

    private static void validateSocket(final Socket socketTCP) {
        if (socketTCP == null) {
            throw new IllegalStateException("TCP socket is null");
        }

        if (!socketTCP.isConnected()) {
            throw new IllegalStateException("TCP socket is not connected");
        }
    }

    private static void fillBufferFromStream(final InputStream stream, final byte[] buffer) throws IOException {
        int cursor = 0;
        while (cursor < buffer.length) {
            int r = stream.read(buffer, cursor, buffer.length - cursor);
            if (r == -1) {
                throw new IOException("failed to read bytes from stream");
            } else {
                cursor += r;
            }
        }
    }

}
