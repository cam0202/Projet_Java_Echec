package chess.server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeoutException;

import chess.network.MessagePacket;
import chess.network.MessageTCP;
import chess.protocol.Message;

class ListenerTCP extends ServerRunner {

    private final Socket socketTCP;
    private final Queue<MessagePacket> queue;

    private Callback callback;

    public ListenerTCP(final Socket socketTCP) {
        this.socketTCP = socketTCP;
        this.queue = new ArrayDeque<>();

        this.callback = null;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public MessagePacket receive() {
        synchronized (this.queue) {
            while (this.queue.isEmpty()) {
                try {
                    this.queue.wait();
                } catch (InterruptedException ignore) {
                }
            }

            return this.queue.poll();
        }
    }

    @Override
    public void run() {
        while (!this.shouldStop()) {
            try {
                MessagePacket packet = MessageTCP.receive(this.socketTCP);

                if (packet.getMessage().getType() == Message.Type.PUSH) {
                    if (this.callback != null ) {
                        this.callback.onUpdate(packet.getMessage().getData());
                    }
                } else {
                    synchronized (this.queue) {
                        this.queue.add(packet);
                        this.queue.notifyAll();
                    }
                }

            } catch (TimeoutException expected) {
                continue;
            } catch (IOException e) {
                throw new RuntimeException("TODO");
            }
        }
    }

}
