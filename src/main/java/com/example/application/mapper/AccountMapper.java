package com.example.application.mapper;

import com.example.application.dto.AccountDto;
import com.example.application.dto.SpecificAccDto;
import com.example.application.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    AccountDto toDto(Account account);

    @Mapping(target = "accountNumber", ignore = true)
    Account toEntity(AccountDto dto);

    SpecificAccDto toSpecificDto(Account accounts);
}
