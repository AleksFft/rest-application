package com.example.application.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
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
    private Integer pin;

    @Column(name = "account_number", nullable = false, unique = true)
    private Integer accountNumber;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;
}
