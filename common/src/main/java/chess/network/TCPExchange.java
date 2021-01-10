package chess.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import chess.protocol.Message;

public class TCPExchange {

    public static void send(Socket socketTCP, Message message) throws IOException {
        validateSocket(socketTCP);

        if (message == null) {
            throw new IllegalArgumentException("Message is null");
        }

        BufferedOutputStream out = new BufferedOutputStream(socketTCP.getOutputStream());
        out.write(message.getBytes());
        out.flush();
    }

    public static Message receive(Socket socketTCP) throws IOException {
        validateSocket(socketTCP);

        BufferedInputStream in = new BufferedInputStream(socketTCP.getInputStream());

        byte[] header = new byte[Message.HEADER_SIZE];
        fillBufferFromStream(in, header);

        Message message = new Message(header);

        byte[] payload = new byte[message.getAmountOfMissingDataBytes()];
        if (payload.length > 0) {
            fillBufferFromStream(in, payload);
            message.setDataBytes(payload);
        }

        return message;
    }

    private static void validateSocket(Socket socketTCP) {
        if (socketTCP == null) {
            throw new IllegalStateException("TCP socket is null");
        }

        if (!socketTCP.isConnected()) {
            throw new IllegalStateException("TCP socket is not connected");
        }
    }

    private static void fillBufferFromStream(InputStream stream, byte[] buffer) throws IOException {
        int cursor = 0;
        while (cursor < buffer.length) {
            int r = stream.read(buffer, cursor, buffer.length - cursor);
            if (r == -1) {
                throw new IOException("Failed to read bytes from stream");
            } else {
                cursor += r;
            }
        }
    }

}
