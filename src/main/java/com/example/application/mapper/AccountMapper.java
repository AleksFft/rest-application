package com.example.application.mapper;

import com.example.application.dto.AccountDto;
import com.example.application.dto.ShowAccountDto;
import com.example.application.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    Account toEntity(AccountDto dto);

    ShowAccountDto toShowAccountDto(Account accounts);
}
