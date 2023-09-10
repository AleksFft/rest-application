package com.example.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferDto extends DepositDto {

    private String sourceAccName;
    private String sourceAccPin;

    public TransferDto(String nameDestinationAccount, String destinationAccPin, Long balance, String sourceAccName, String sourceAccPin) {
        super(nameDestinationAccount, destinationAccPin, balance);
        this.sourceAccName = sourceAccName;
        this.sourceAccPin = sourceAccPin;
    }
}
