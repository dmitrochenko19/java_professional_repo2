package ru.otus.processor;

import ru.otus.model.Message;

import java.time.LocalDateTime;

public class ThrowExceptionProcessor implements Processor{
    @Override
    public Message process(Message message) {
        if (getSecond() % 2 == 0)
        {
            throw new RuntimeException("Even second!");
        }
        return message;
    }

    public int getSecond() {
        return LocalDateTime.now().getSecond();
    }
}
