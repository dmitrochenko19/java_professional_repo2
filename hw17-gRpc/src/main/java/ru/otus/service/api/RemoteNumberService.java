package ru.otus.service.api;

import io.grpc.stub.StreamObserver;

public interface RemoteNumberService {
    void runNums(RemoteNumberRequest request, StreamObserver<RemoteNumberResponse> responseObserver);
}