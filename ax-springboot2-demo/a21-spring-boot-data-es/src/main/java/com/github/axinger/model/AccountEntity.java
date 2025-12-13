package com.github.axinger.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity implements Serializable {
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
