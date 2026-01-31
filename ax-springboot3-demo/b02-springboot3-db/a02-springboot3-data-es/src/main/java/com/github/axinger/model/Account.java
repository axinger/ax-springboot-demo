package com.github.axinger.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Account {
    private long accountNumber;
    private long balance;
    private String firstname;
    private String lastname;
    private long age;
    private String gender;
    private String address;
    private String employer;
    private String email;
    private String city;
    private String state;

}
