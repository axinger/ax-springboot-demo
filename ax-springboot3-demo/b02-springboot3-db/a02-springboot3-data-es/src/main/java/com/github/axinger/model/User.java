package com.github.axinger.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName User.java
 * @Description TODO
 * @createTime 2022年02月24日 15:41:00
 */

@Data
@Accessors(chain = true)
@Builder
public class User {

    private String userName;
    private String gender;
    private Integer age;
}
