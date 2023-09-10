package com.example.application.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "account_name", nullable = false)
    private String name;

    @Column(name = "pin", nullable = false)
    private String pin;

    @Column(name = "account_number", nullable = false, unique = true)
    private Integer accountNumber;

    @Column(name = "balance")
    private Long balance;
}
