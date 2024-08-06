package ru.otus.services;


import ru.otus.crm.model.User;
import ru.otus.dao.UsersDao;

public class UserAuthServiceImpl implements UserAuthService {

    private final UsersDao userDao;

    public UserAuthServiceImpl(UsersDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean authenticate(String login, String password) {
        User user = userDao.findByLogin(login);
        return user != null && user.getPassword().equals(password);
    }
}
