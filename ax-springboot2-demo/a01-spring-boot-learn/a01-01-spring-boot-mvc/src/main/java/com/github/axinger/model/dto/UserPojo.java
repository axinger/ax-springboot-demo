package com.github.axinger.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserPojo<T> {
    private Long id;
    private String name;
    private Long age;
    private int sex = 0;

    private List<T> data;


    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class BookPojo {
        private Long id;
        private String name;
    }
}
