package ru.otus.service.app;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ru.otus.service.api.ClientService;
import ru.otus.service.impl.ClientServiceImpl;


public class GRPCClient {
    public static final int PORT = 8190;
    public static final String HOST = "localhost";

    public static void main(String[] args) {

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(HOST, PORT)
                .usePlaintext()
                .build();

        ClientService numberClient = new ClientServiceImpl(channel);
        numberClient.run();

        channel.shutdown();
    }
}