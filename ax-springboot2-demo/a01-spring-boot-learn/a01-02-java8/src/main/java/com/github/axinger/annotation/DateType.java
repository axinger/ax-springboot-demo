package com.github.axinger.annotation;

import lombok.Getter;

@Getter
public enum DateType {

    VARCHAR;


    private int len;

    public DateType len(int len) {
        this.len = len;
        return this;
    }

    public DateType len() {
        return this;
    }

}
