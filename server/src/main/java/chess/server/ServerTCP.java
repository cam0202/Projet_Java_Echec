package chess.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

class ServerTCP extends Thread {
    private final static Logger LOGGER = Logger.getLogger(ServerTCP.class.getName());

    private final int port;

    public ServerTCP(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try (ServerSocket socket = new ServerSocket(port)) {
            while (!this.isInterrupted()) {
                Socket client = socket.accept();
                
                /*
                * Start a worker per-client. This is fine for a small app like this one, but we
                * should create a worker pool if we want to scale up.
                */
                Thread worker = new Thread(new WorkerTCP(client));
                worker.start();

                LOGGER.info("Client connected [" + client.getInetAddress().getHostAddress() + "]:" + client.getPort());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    private class WorkerTCP extends Worker implements Runnable {
        private Socket client;

        WorkerTCP(Socket client) {
            this.client = client;
        }

        private void fillBuffer(InputStream stream, byte[] buffer) throws IOException {
            int cursor = 0;
            while (cursor < buffer.length) {
                int r = stream.read(buffer, cursor, buffer.length - cursor);
                if (r == -1) {
                    // ERROR
                } else {
                    cursor += r;
                }
            }
        }

        @Override
        public void run() {
            try {
                try (BufferedInputStream in = new BufferedInputStream(client.getInputStream());
                        BufferedOutputStream out = new BufferedOutputStream(client.getOutputStream())) {

                    // Read header size
                    byte[] header = new byte[chess.protocol.Header.SIZE_ENCODED];
                    byte[] payload;
                    while (!isInterrupted()) {
                        fillBuffer(in, header);
                        chess.protocol.Header decodedHeader = chess.protocol.Header.decode(header);

                        payload = new byte[decodedHeader.getPayloadLength()];
                        fillBuffer(in, payload);
                        chess.protocol.Payload decodedPayload = chess.protocol.Payload.decode(payload);

                        chess.protocol.Message message = new chess.protocol.Message(decodedHeader, decodedPayload);
                    }
                }

                client.close();
                LOGGER.info("Client disconnected [" + client.getInetAddress().getHostAddress() + "]:" + client.getPort());

            } catch (IOException e) {
                LOGGER.log(Level.WARNING, e.getMessage(), e);
            }
        }
    }
}
