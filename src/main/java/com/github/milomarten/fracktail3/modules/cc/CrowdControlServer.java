package com.github.milomarten.fracktail3.modules.cc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CrowdControlServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrowdControlServer.class);

    @Autowired
    private ObjectMapper objectMapper;

    private Listener listener;

    private final AtomicLong id = new AtomicLong(1);

    private final Sinks.Many<Integer> subscriptions = Sinks.many().multicast().directBestEffort();

    public void start() throws IOException {
        if(!isRunning()) {
            try {
                listener = new Listener(new ServerSocket(8081));
                new Thread(listener).start();
            } catch (IOException e) {
                listener = null;
                throw e;
            }
        }
    }

    public Flux<Integer> getSubscriptions() {
        return subscriptions.asFlux();
    }

    public void stop() throws IOException {
        if(isRunning()) {
            listener.stop();
            listener = null;
        }
    }

    public RedemptionResult send(CCRequestModel request) {
        if(isRunning()) {
            CCRequest toRCT = new CCRequest(id.getAndIncrement(), request);
            return listener.send(toRCT);
        }
        return RedemptionResult.of(RedemptionStatus.FAIL, "Server is not running.");
    }

    public boolean isRunning() {
        return listener != null;
    }

    @RequiredArgsConstructor
    private class Listener implements Runnable {
        private final ServerSocket server;
        public boolean stop;

        private final List<Socket> sockets = Collections.synchronizedList(new ArrayList<>());

        @Override
        public void run() {
            do {
                try {
                    Socket socket = server.accept();
                    LOGGER.info("Received connection from " + socket.getInetAddress());
                    subscriptions.emitNext(0, Sinks.EmitFailureHandler.FAIL_FAST);
                    sockets.add(socket);
                } catch (SocketException e) {
                    break;
                } catch (IOException e) {
                    LOGGER.error("Error accepting socket", e);
                    break;
                }
            } while (!stop);
        }

        public void stop() throws IOException {
            this.stop = true;
            if(!this.server.isClosed()) {
                this.server.close();
            }
        }

        public RedemptionResult send(CCRequest request) {
            synchronized (sockets) {
                return sockets.parallelStream()
                        .map(socket -> send(request, socket))
                        .reduce(
                                RedemptionResult.of(RedemptionStatus.NONE, "No connections available"),
                                RedemptionResult::combine
                        );
            }
        }

        private RedemptionResult send(CCRequest request, Socket socket) {
            try {
                String json = objectMapper.writeValueAsString(request) + "\n";
                OutputStream os = socket.getOutputStream();
                os.write(json.getBytes());
                os.flush();
                return RedemptionResult.of(RedemptionStatus.OK, "Successful!");
            } catch (JsonProcessingException e) {
                return RedemptionResult.of(RedemptionStatus.FAIL, "Error converting to JSON: " + e.getMessage());
            } catch (IOException e) {
                return RedemptionResult.of(RedemptionStatus.FAIL, "Error getting OutputStream: " + e.getMessage());
            }
        }
    }
}
