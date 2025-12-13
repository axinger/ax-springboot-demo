package com.github.axinger;

/**
 * 抛异常接口
 **/
@FunctionalInterface
public interface DoFunction {

    /**
     * 抛出异常信息
     *
     * @param message 异常信息
     * @return void
     **/
    void message(String message);
}
