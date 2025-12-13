package com.github.axinger.model.dto;

import com.github.axinger.model.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person {
    private int id;
    private String name;
    private Integer age;

    //    @EnumValidator(value = Gender.class)
    private Gender gender;
}
