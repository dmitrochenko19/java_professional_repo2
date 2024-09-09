package ru.otus.service.impl;

import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.observers.NumbersObserver;
import ru.otus.service.api.ClientService;
import ru.otus.service.RemoteNumberRequest;
import ru.otus.service.RemoteNumberServiceGrpc;

import static ru.otus.Utils.sleep;

public class ClientServiceImpl implements ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    public static final long FIRST_VALUE = 0;
    public static final long LAST_VALUE = 30;
    public static final long CLIENT_FIRST_VALUE = 0;
    public static final long CLIENT_LAST_VALUE = 50;
    private final RemoteNumberServiceGrpc.RemoteNumberServiceStub remoteNumberService;
    private long currentValue = 0;

    public ClientServiceImpl(ManagedChannel channel) {
        this.remoteNumberService = RemoteNumberServiceGrpc.newStub(channel);
    }

    public void run() {
        NumbersObserver observer = new NumbersObserver();
        RemoteNumberRequest request = RemoteNumberRequest.newBuilder()
                .setFirstValue(FIRST_VALUE)
                .setLastValue(LAST_VALUE)
                .build();
        remoteNumberService.runNums(request, observer);

        for (long i = CLIENT_FIRST_VALUE; i < CLIENT_LAST_VALUE; i++) {
            long numberFromServer = observer.getValueAndChange();
            currentValue = currentValue + numberFromServer + 1;
            logger.info("current value %d".formatted(currentValue));
            sleep(1);
        }
    }
}