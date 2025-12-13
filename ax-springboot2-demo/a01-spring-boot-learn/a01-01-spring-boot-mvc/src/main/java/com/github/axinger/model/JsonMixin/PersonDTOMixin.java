package com.github.axinger.model.JsonMixin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.axinger.model.dto.PersonDTO;
import org.springframework.boot.jackson.JsonMixin;

@JsonMixin(PersonDTO.class) // 1
public abstract class PersonDTOMixin {
    @JsonProperty("fullName") // 2
    String name; // 3
}
