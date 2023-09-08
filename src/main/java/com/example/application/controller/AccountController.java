package com.example.application.controller;

import com.example.application.dto.AccountDto;
import com.example.application.dto.SpecificAccDto;
import com.example.application.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;

    @PostMapping(value = "/save")
    public AccountDto save(@RequestBody AccountDto dto) {
        return service.save(dto);
    }

    @GetMapping
    public List<SpecificAccDto> save() {
        return service.getAll();
    }
}
