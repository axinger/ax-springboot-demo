package com.github.axinger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName com.ax.demo.Person.java
 * @description TODO
 * @createTime 2022年06月12日 03:01:00
 */
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
