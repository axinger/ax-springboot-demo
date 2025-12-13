package com.github.axinger.log;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Person {

    private static final Logger log = LoggerFactory.getLogger(Person.class);

    public static void main(String[] args) {

        log.debug("log.debug....................");
        log.info("log.info....................");
        log.warn("log.warn....................");
    }

    public void add() {

    }
}

