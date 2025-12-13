package com.github.axinger;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 *
 * @author zlc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {

    /**
     * id
     */
    @Alias("主键")
    private Integer id;

    /**
     * 姓名
     */
    @Alias("姓名")
    private String name;

    /**
     * 性别
     */
    @Alias("性别")
    private String gender;

    private Dog dog;
    private Cat cat;

    @Alias("生日")
    private Date birthday;

    @SuppressWarnings("unused")
    public String getBirthday() {
        return DateUtil.format(birthday, "yyyy-MM-dd HH");
    }
}
