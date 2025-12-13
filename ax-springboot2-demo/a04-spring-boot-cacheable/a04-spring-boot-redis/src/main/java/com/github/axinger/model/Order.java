package com.github.axinger.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
    private String id;
    private String name;

    private LocalDateTime dateTime;
}
