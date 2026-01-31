package com.github.axinger.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageUserDTO implements Serializable {

    private String guild;

    private String name;

    private int age;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date currentTime;

}
