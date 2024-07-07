package ru.otus.processor;

import java.time.LocalDateTime;

public class TimeProviderImpl implements TimeProvider{
    @Override
    public int getSecond() {
        return LocalDateTime.now().getSecond();
    }
}