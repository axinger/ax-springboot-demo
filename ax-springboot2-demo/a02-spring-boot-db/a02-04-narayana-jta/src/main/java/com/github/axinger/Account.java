package com.github.axinger;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "sys_account")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private BigDecimal balance;


}
