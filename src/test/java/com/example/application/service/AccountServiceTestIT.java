package com.example.application.service;

import com.example.application.dto.AccountDto;
import com.example.application.dto.DepositDto;
import com.example.application.dto.TransferDto;
import com.example.application.exception.AccountException;
import com.example.application.model.Account;
import com.example.application.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class AccountServiceTestIT extends AbstractTestNGSpringContextTests {

    @Autowired
    private AccountRepository repository;
    @Autowired
    private AccountService service;

    @BeforeMethod
    public void init() {
        repository.saveAll(List.of(account("bob", 0L), account("ris", 100L)));
    }

    @AfterMethod
    public void clean() {
        repository.deleteAll();
    }

    @Test
    public void create_accountDtoIsValid_createdSuccessfully() {
        AccountDto accountDto = new AccountDto("name", "1111");

        service.create(accountDto);

        assertThat(repository.findAll().size()).isEqualTo(3);
    }

    @Test(expectedExceptions = AccountException.class)
    public void create_pinInInvalidFormat_throwException() {
        AccountDto accountDto = new AccountDto("name", "dfgfdg");

        service.create(accountDto);
    }

    @Test
    public void deposit_dtoIsValid_depositedSuccessfully() {
        DepositDto depositDto = new DepositDto("bob", "1111", 55L);

        service.deposit(depositDto);

        assertThat(repository.findByName("bob").getBalance()).isEqualTo(55L);
    }

    @Test(expectedExceptions = AccountException.class)
    public void deposit_depositSumIsNegative_throwException() {
        DepositDto depositDto = new DepositDto("bob", "1111", -55L);

        service.deposit(depositDto);
    }

    @Test(expectedExceptions = AccountException.class)
    public void deposit_pinNotValid_throwException() {
        DepositDto depositDto = new DepositDto("bob", "2222", 55L);

        service.deposit(depositDto);
    }

    @Test
    public void withdraw_dtoIsValid_withdrewSuccessfully() {
        DepositDto depositDto = new DepositDto("ris", "1111", 1L);

        service.withdraw(depositDto);

        assertThat(repository.findByName("ris").getBalance()).isEqualTo(99L);
    }

    @Test(expectedExceptions = AccountException.class)
    public void withdraw_notEnoughMoney_throwException() {
        DepositDto depositDto = new DepositDto("bob", "1111", 1L);

        service.withdraw(depositDto);
    }

    @Test(expectedExceptions = AccountException.class)
    public void withdraw_pinNotValid_throwException() {
        DepositDto depositDto = new DepositDto("ris", "8888", 1L);

        service.withdraw(depositDto);
    }

    @Test
    public void transfer_transferDtoIsValid_transferredSuccessfully() {
        TransferDto transferDto = new TransferDto(
                "bob", "1111", 25L, "ris", "1111");

        service.transfer(transferDto);

        assertThat(repository.findByName("ris").getBalance()).isEqualTo(75L);
        assertThat(repository.findByName("bob").getBalance()).isEqualTo(25L);
    }

    @Test(expectedExceptions = AccountException.class)
    public void transfer_notEnoughMoneyOnSourceAcc_throwException() {
        TransferDto transferDto = new TransferDto(
                "bob", "1111", 250L, "ris", "1111");

        service.transfer(transferDto);
    }

    @Test(expectedExceptions = AccountException.class)
    public void transfer_destinationAccPinIsInvalid_throwException() {
        TransferDto transferDto = new TransferDto(
                "bob", "2222", 250L, "ris", "1111");

        service.transfer(transferDto);
    }

    @Test(expectedExceptions = AccountException.class)
    public void transfer_sourceAccPinIsInvalid_throwException() {
        TransferDto transferDto = new TransferDto(
                "bob", "1111", 250L, "ris", "2222");

        service.transfer(transferDto);
    }

    @Test
    public void findAll_returnExpectedListSize() {
        assertThat(service.findAll().size()).isEqualTo(2);
    }

    private Account account(String name, Long balance) {
        Account account = new Account();
        account.setPin("1111");
        account.setBalance(balance);
        account.setName(name);
        account.setAccountNumber(555555);

        return account;
    }
}