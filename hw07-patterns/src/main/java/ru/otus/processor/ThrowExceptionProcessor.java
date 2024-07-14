package ru.otus.processor;

import ru.otus.model.Message;


public class ThrowExceptionProcessor implements Processor{

    private final TimeProvider timeProvider;

    public ThrowExceptionProcessor(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public Message process(Message message) {
        if (timeProvider.getSecond() % 2 == 0)
        {
            throw new RuntimeException("Even second!");
        }
        return message;
    }
}
