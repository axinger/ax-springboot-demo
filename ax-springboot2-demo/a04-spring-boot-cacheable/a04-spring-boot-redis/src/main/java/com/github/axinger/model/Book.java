package com.github.axinger.model;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book implements Serializable {
    private Integer id;
    private String name;
}
