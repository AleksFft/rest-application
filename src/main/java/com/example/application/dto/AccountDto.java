package com.example.application.dto;

import lombok.Data;

@Data
public class AccountDto extends SpecificAccDto {
    private Integer pin;
    private Integer accountNumber;
}
