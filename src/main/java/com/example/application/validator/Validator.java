package com.example.application.validator;

import com.example.application.exception.AccountException;
import com.example.application.model.Account;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.regex.Pattern;

@Component
public class Validator {

    private static final String PIN_FORMAT_ERROR_MESSAGE = "The pin should contains four digits, bot your is: %s";
    private static final String PIN_INVALID_ERROR_MESSAGE = "The pin is invalid, your is: %s";
    private static final String BALANCE_ERROR_MESSAGE = "You printed negative sum: %s";
    private static final String NOT_ENOUGH_MONEY_ERROR_MESSAGE =
            "Not enough money on your balance, " +
                    "try to withdraw: %d " +
                    "actual balance: %d";

    public void validatePin(String pin, Account foundAccount) {
        validatePin(pin);
        if (!Objects.equals(foundAccount.getPin(), pin)) {
            throw new AccountException(String.format(PIN_INVALID_ERROR_MESSAGE, pin));
        }
    }

    public void validatePin(String pin) {
        if (Objects.isNull(pin) || pin.length() != 4) {
            throw new AccountException(String.format(PIN_FORMAT_ERROR_MESSAGE, pin));
        }

        Pattern pattern = Pattern.compile("[0-9]+");
        if (!pattern.matcher(pin).matches()) {
            throw new AccountException(String.format(PIN_FORMAT_ERROR_MESSAGE, pin));
        }
    }

    public void validateBalance(Long amount) {
        if (amount < 0) {
            throw new AccountException(String.format(BALANCE_ERROR_MESSAGE, amount));
        }
    }

    public void validateOnEnoughMoney(Long withdrawAmount, Long balance) {
        if (withdrawAmount > balance) {
            throw new AccountException(String.format(NOT_ENOUGH_MONEY_ERROR_MESSAGE, withdrawAmount, balance));
        }
    }
}
