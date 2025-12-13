package com.github.axinger.model;

import lombok.Data;
import lombok.Value;
import lombok.With;

@Value
@Data
public class ImmutablePerson {
    @With
    String name;
//    int age;

    @With
    int age;
}
