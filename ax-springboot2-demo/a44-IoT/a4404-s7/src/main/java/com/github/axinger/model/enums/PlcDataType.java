package com.github.axinger.model.enums;

import lombok.Getter;

@Getter
public enum PlcDataType {

    REAL("DBD", "REAL", Float.class),
    WORD("DBW", "WORD", Integer.class),
    BOOL("DBX", "BOOL", Boolean.class),

    ;


    private final String code;
    private final String type;
    private final Class<?> aClass;

    PlcDataType(String code, String type, Class<?> aClass) {
        this.code = code;
        this.type = type;
        this.aClass = aClass;
    }

}
