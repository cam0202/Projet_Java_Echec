package chess.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

import chess.protocol.Message;

public class UDPExchange {

    public static void send(DatagramSocket socketUDP, ExchangePacket packet) throws IOException {
        validateSocket(socketUDP);

        byte[] bytes = packet.getMessage().getBytes();
        DatagramPacket p = new DatagramPacket(bytes, bytes.length, packet.getAddress(), packet.getPort());
        socketUDP.send(p);
    }

    public static ExchangePacket receive(DatagramSocket socketUDP) throws IOException {
        validateSocket(socketUDP);

        ByteBuffer buffer = ByteBuffer.allocate(Message.MAX_LENGTH);
        DatagramPacket packet = new DatagramPacket(buffer.array(), buffer.array().length);
        socketUDP.receive(packet);

        byte[] header = new byte[Message.HEADER_SIZE];
        buffer.get(header);

        ExchangePacket p = new ExchangePacket(packet.getAddress(), packet.getPort(), new Message(header));

        byte[] payload = new byte[p.getMessage().getAmountOfMissingDataBytes()];
        buffer.get(payload);

        p.getMessage().setDataBytes(payload);

        return p;
    }

    private static void validateSocket(DatagramSocket socketUDP) {
        if (socketUDP == null) {
            throw new IllegalStateException("UDP socket is null");
        }
    }

}
