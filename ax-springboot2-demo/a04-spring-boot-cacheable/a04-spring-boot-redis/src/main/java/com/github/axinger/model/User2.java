package com.github.axinger.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User2 implements Serializable {

    private Integer id;
    private String name;
    private Integer age;

    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;


    private List<Book> books;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Book implements Serializable {
        private Integer id;
        private String name;
    }
}

