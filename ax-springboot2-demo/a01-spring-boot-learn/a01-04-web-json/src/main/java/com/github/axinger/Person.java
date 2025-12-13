package com.github.axinger;


import com.github.axinger.dto.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Person {

    private String id;
    private String name;
    private int age;

    private Gender gender;
    private List<Book> books;
}
