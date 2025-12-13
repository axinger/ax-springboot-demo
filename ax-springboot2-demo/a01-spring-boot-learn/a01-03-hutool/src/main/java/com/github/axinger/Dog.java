package com.github.axinger;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dog implements Serializable {

    private String name;
    private Integer age;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
