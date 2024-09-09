package ru.otus.service.impl;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.Utils;
import ru.otus.service.api.RemoteNumberService;
import ru.otus.service.app.GRPCClient;
import ru.otus.service.RemoteNumberRequest;
import ru.otus.service.RemoteNumberResponse;
import ru.otus.service.RemoteNumberServiceGrpc;


public class RemoteNumberServiceImpl extends RemoteNumberServiceGrpc.RemoteNumberServiceImplBase implements RemoteNumberService {

    private static final Logger logger = LoggerFactory.getLogger(GRPCClient.class);

    @Override
    public void runNums(RemoteNumberRequest request, StreamObserver<RemoteNumberResponse> responseObserver) {
        for (long i = request.getFirstValue(); i < request.getLastValue(); i++) {
            var response = RemoteNumberResponse.newBuilder().setNumber(i).build();
            responseObserver.onNext(response);
            Utils.sleep(2);
        }
        responseObserver.onCompleted();
    }
}