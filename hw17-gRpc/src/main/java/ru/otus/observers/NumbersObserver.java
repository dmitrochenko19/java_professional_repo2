package ru.otus.observers;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.service.RemoteNumberResponse;

public class NumbersObserver implements StreamObserver<RemoteNumberResponse> {

    private static final Logger logger = LoggerFactory.getLogger(NumbersObserver.class);

    private long responseValue = 0;

    @Override
    public void onNext(RemoteNumberResponse numberResponse) {
        setValue(numberResponse.getNumber());
        logger.info("new value: %d".formatted(numberResponse.getNumber()));
    }

    @Override
    public void onError(Throwable throwable) {
        logger.error("Error",  throwable);
    }

    @Override
    public void onCompleted() {
        logger.info("Completed");
    }

    public synchronized void setValue(long responseValue) {
        this.responseValue = responseValue;
    }

    public synchronized long getValueAndChange() {
        final long value = this.responseValue;
        this.responseValue = 0;
        return value;
    }
}