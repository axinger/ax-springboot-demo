package com.github.axinger;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName DateModel.java
 * @description TODO
 * @createTime 2022年07月28日 17:47:00
 */

@Data
public class DateModel {
    private String name;
    private LocalDateTime dateTime;
}
