package com.github.axinger.sys.vo;

import com.github.axinger.sys.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysPersonVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    private Long id;

    private String name;

    private Integer age;

    @Builder.Default()
    private Gender gender = Gender.unknown;

    private LocalDateTime birthday;


    private LocalDateTime createTime;


    private LocalDateTime updateTime;

    @Builder.Default()
    private Long version = 1L;

    @Builder.Default()
    private int deleted = 0;


}
