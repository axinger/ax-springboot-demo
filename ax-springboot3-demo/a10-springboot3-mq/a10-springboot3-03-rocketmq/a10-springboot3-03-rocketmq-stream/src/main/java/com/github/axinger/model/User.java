package com.github.axinger.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class User implements Serializable {
    @Serial
    private final static long serialVersionUID = 1L;
    private Long id;

    private String name;

}
