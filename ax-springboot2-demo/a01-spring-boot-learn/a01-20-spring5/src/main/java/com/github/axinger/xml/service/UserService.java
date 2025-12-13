package com.github.axinger.xml.service;

import com.github.axinger.xml.dao.UserDao;

public class UserService {

    private UserDao userDao;

    public void add() {
        userDao.update();
    }
}
