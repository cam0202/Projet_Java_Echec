package chess.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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

        private void fillBuffer(Reader reader, char[] buffer) throws IOException {
            int cursor = 0;
            while (cursor < buffer.length) {
                int r = reader.read(buffer, cursor, buffer.length - cursor);
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
                try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))) {

                    // Read header size
                    char[] header = new char[chess.protocol.Header.SIZE];
                    char[] payload;
                    while (!isInterrupted()) {
                        fillBuffer(in, header);
                        // TODO: process header and get payload size
                        payload = new char[0];
                        fillBuffer(in, payload);
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
