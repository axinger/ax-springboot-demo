package com.github.axinger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person implements Serializable {

    private Integer id;

    private String name;

    private Boolean big;

    private Integer age;

    private boolean sex;

    public String log1() {
        return "";
    }
}
