package com.github.axinger.bean;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Gender {

    none(10, "未知"),
    male(11, "男性"),
    female(12, "女性");


    private int id;
    private String code;

    @JsonValue
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return code;
    }
}
