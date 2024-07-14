package ru.otus.listener.homework;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> historyHolder = new HashMap<>();

    private long count = 1;

    @Override
    public void onUpdated(Message msg) {
        historyHolder.put(count, msg);
        count++;
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.of(historyHolder.get(id));
    }
}
