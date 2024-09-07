package ru.otus.dao;

import ru.otus.crm.model.User;
import ru.otus.database.InMemoryUserDatabase;

public class UsersDaoImpl implements UsersDao {
    private final InMemoryUserDatabase database;

    public UsersDaoImpl(InMemoryUserDatabase database) {
        this.database = database;
    }

    @Override
    public User findByLogin(String login) {
        return database.getUsers().values().stream()
                .filter(user -> user.getLogin().equals(login)).findFirst().orElse(null);
    }
}
