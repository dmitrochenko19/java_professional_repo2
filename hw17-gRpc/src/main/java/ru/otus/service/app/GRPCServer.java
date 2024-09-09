package ru.otus.service.app;

import io.grpc.BindableService;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.service.RemoteNumberService;
import ru.otus.service.impl.RemoteNumberServiceImpl;

import java.io.IOException;

public class GRPCServer {
    public static final int PORT = 8190;

    private static final Logger logger = LoggerFactory.getLogger(GRPCServer.class);

    public static void main(String[] args) {
        RemoteNumberService remoteNumberService = new RemoteNumberServiceImpl();
        io.grpc.Server server = ServerBuilder
                .forPort(PORT)
                .addService((BindableService) remoteNumberService)
                .build();
        try {
            server.start();
            server.awaitTermination();
        } catch (IOException | InterruptedException e) {
            logger.error("Error happened while starting server", e);
        }
    }
}