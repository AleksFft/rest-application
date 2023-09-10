package com.example.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositDto extends AccountDto {
    private Long balance;

    public DepositDto(String name, String pin, Long balance) {
        super(name, pin);
        this.balance = balance;
    }
}
