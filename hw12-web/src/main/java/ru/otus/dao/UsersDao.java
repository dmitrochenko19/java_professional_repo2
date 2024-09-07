package ru.otus.dao;

import ru.otus.crm.model.User;

public interface UsersDao {
    User findByLogin(String login);
}
