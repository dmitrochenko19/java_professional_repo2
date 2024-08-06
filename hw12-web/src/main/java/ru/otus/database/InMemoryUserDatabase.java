package ru.otus.database;

import ru.otus.crm.model.User;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserDatabase {
    private final Map<Long, User> users;

    public InMemoryUserDatabase() {
        this.users = new HashMap<>();
        users.put(1L, new User("login", "password"));
    }

    public Map<Long, User> getUsers() {
        return users;
    }
}
