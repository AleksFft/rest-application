package com.example.application.controller;

import com.example.application.dto.AccountDto;
import com.example.application.dto.DepositDto;
import com.example.application.dto.ShowAccountDto;
import com.example.application.dto.TransferDto;
import com.example.application.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;

    @PostMapping(value = "/create")
    public void create(@RequestBody AccountDto dto) {
        service.create(dto);
    }

    @PostMapping(value = "/deposit")
    public void deposit(@RequestBody DepositDto dto) {
        service.deposit(dto);
    }

    @PostMapping(value = "/withdraw")
    public void withdraw(@RequestBody DepositDto dto) {
        service.withdraw(dto);
    }

    @PostMapping(value = "/transfer")
    public void transfer(@RequestBody TransferDto dto) {
        service.transfer(dto);
    }

    @GetMapping
    public List<ShowAccountDto> getAll() {
        return service.findAll();
    }
}
