package com.github.axinger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyLog {
    private final org.slf4j.Logger log;
    //
    public Logger logA = org.slf4j.LoggerFactory.getLogger("A_SERVICE_LOG");
    public Logger logB = org.slf4j.LoggerFactory.getLogger("B_SERVICE_LOG");
//    public Logger logA ;
//    public  Logger logB;

    @SuppressWarnings("all")
    public MyLog(String name) {
        this.log = LoggerFactory.getLogger(name);
//        this.logA = LoggerFactory.getLogger(name);
//        this.logB = LoggerFactory.getLogger(name);
    }

    public MyLog(Class<?> clazz) {
        this.log = LoggerFactory.getLogger(clazz);
//        String temp = clazz.getPackageName()+" - A_SERVICE_LOG";
//        this.logA = LoggerFactory.getLogger(temp);
//        this.logB = LoggerFactory.getLogger("B_SERVICE_LOG "+clazz);
    }

    @SuppressWarnings("all")
    public static MyLog getLogger(Class<?> clazz) {
        return new MyLog(clazz);
    }

    public void info(String format, Object... args) {
        log.info(format, args);
    }

    public void error(String format, Object... args) {
        log.error(format, args);
    }


    public void debug(String format, Object... args) {
        log.debug(format, args);
    }


    public void trace(String format, Object... args) {
        log.trace(format, args);
    }

    public void warn(String format, Object... args) {
        log.warn(format, args);
    }

}
