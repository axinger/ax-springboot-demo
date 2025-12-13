package com.github.axinger.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements Serializable {

    private Integer id;
    private String name;
    private Integer age;

    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    //@JsonSerialize(using = LocalDateTimeSerializer.class)
    //@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    // @Transient
    @Builder.Default
    private LocalDateTime localDateTime = LocalDateTime.now();
    private List<User.Book> books;

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

