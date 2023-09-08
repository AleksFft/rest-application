package com.example.application.service;

import com.example.application.db.AccountRepository;
import com.example.application.dto.AccountDto;
import com.example.application.dto.SpecificAccDto;
import com.example.application.mapper.AccountMapper;
import com.example.application.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;

    public AccountDto save(AccountDto dto) {
        validatePin(dto.getPin());

        Account account = AccountMapper.INSTANCE.toEntity(dto);
        account.setAccountNumber(generateAccNumber());
        return AccountMapper.INSTANCE.toDto(repository.save(account));
    }

    public List<SpecificAccDto> getAll() {
        return repository.findAll()
                .stream()
                .map(AccountMapper.INSTANCE::toSpecificDto)
                .collect(Collectors.toList());
    }

    private int generateAccNumber() {
        return new Random().nextInt(999_999_999 - 99_999_999 + 1) + 99_999_999;
    }

    private void validatePin(int pin) {
        if (pin < 1000 || pin > 9999) {
            throw new  RuntimeException("incorrect pin");
        }
    }
}
