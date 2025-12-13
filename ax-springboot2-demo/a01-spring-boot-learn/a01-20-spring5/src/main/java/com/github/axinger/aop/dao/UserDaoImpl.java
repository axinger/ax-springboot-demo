package com.github.axinger.aop.dao;

public class UserDaoImpl implements UserDao {

    @Override
    public Integer add(Integer a, Integer b) {

        return a + b;
    }

    @Override
    public String update(String id) {

        return id;

    }
}
