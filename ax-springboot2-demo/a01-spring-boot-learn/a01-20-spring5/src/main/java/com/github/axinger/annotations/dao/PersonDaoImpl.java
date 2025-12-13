package com.github.axinger.annotations.dao;

import org.springframework.stereotype.Repository;

@Repository
public class PersonDaoImpl implements PersonDao {
    @Override
    public void add() {
        System.out.println("add.......PersonDaoImpl");
    }
}
