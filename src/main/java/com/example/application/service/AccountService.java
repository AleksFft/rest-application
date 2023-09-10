package com.example.application.service;

import com.example.application.dto.AccountDto;
import com.example.application.dto.DepositDto;
import com.example.application.dto.ShowAccountDto;
import com.example.application.dto.TransferDto;
import com.example.application.mapper.AccountMapper;
import com.example.application.model.Account;
import com.example.application.repository.AccountRepository;
import com.example.application.validator.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;
    private final Validator validator;

    public void create(AccountDto dto) {
        validator.validatePin(dto.getPin());
        Account account = AccountMapper.INSTANCE.toEntity(dto);
        account.setAccountNumber(generateAccNumber());
        account.setBalance(0L);

        repository.save(account);
    }

    public void deposit(DepositDto dto) {
        validator.validateBalance(dto.getBalance());
        Account account = repository.findByName(dto.getName());
        validator.validatePin(dto.getPin(), account);
        account.setBalance(account.getBalance() + dto.getBalance());

        repository.save(account);
    }

    public void withdraw(DepositDto dto) {
        validator.validateBalance(dto.getBalance());
        Account account = repository.findByName(dto.getName());
        validator.validatePin(dto.getPin(), account);
        validator.validateOnEnoughMoney(dto.getBalance(), account.getBalance());
        account.setBalance(account.getBalance() - dto.getBalance());

        repository.save(account);
    }

    public void transfer(TransferDto dto) {
        Long transferSum = dto.getBalance();
        validator.validateBalance(transferSum);

        Account sourceAccount = repository.findByName(dto.getSourceAccName());
        validator.validatePin(dto.getSourceAccPin(), sourceAccount);

        Account destinationAccount = repository.findByName(dto.getName());
        validator.validatePin(dto.getPin(), destinationAccount);
        validator.validateOnEnoughMoney(transferSum, sourceAccount.getBalance());

        sourceAccount.setBalance(sourceAccount.getBalance() - transferSum);
        destinationAccount.setBalance(destinationAccount.getBalance() + transferSum);

        repository.save(sourceAccount);
        repository.save(destinationAccount);
    }

    public List<ShowAccountDto> getAll() {
        return repository.findAll()
                .stream()
                .map(AccountMapper.INSTANCE::toShowAccountDto)
                .collect(Collectors.toList());
    }

    private int generateAccNumber() {
        return new Random().nextInt(999_999_999 - 99_999_999 + 1) + 99_999_999;
    }
}
