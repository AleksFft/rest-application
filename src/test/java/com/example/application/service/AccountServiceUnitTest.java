package com.example.application.service;

import com.example.application.dto.AccountDto;
import com.example.application.dto.DepositDto;
import com.example.application.dto.ShowAccountDto;
import com.example.application.dto.TransferDto;
import com.example.application.exception.AccountException;
import com.example.application.model.Account;
import com.example.application.repository.AccountRepository;
import com.example.application.validator.Validator;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class AccountServiceUnitTest {

    private AutoCloseable closeable;
    private AccountService service;

    @Mock
    private AccountRepository repository;
    @Spy
    private Validator validator;

    @BeforeMethod
    public void init() {
        this.closeable = MockitoAnnotations.openMocks(this);
        service = new AccountService(repository, validator);
        mock(AccountService.class);
        mock(AccountRepository.class);
    }

    @AfterMethod
    public void cleanMock() throws Exception {
        closeable.close();
    }

    @Test
    public void create_accountDtoIsValid_createdSuccessfully() {
        service.create(new AccountDto("bob", "1111"));

        verify(validator, times(1)).validatePin("1111");
    }

    @Test(expectedExceptions = AccountException.class)
    public void create_pinInInvalidFormat_throwException() {
        service.create(new AccountDto("bob", "hgfj"));

        verify(validator, times(1)).validatePin("hgfj");
    }

    @Test
    public void deposit_dtoIsValid_depositedSuccessfully() {
        Account account = account("bob", 0L);
        when(repository.findByName("bob")).thenReturn(account);

        service.deposit(new DepositDto("bob", "1111", 75L));

        assertThat(account.getBalance()).isEqualTo(75L);
        verify(repository, times(1)).findByName("bob");
        verify(validator, times(1)).validatePin("1111");
        verify(validator, times(1)).validateBalance(75L);
    }

    @Test(expectedExceptions = AccountException.class)
    public void deposit_depositSumIsNegative_throwException() {
        Account account = account("bob", 0L);
        when(repository.findByName("bob")).thenReturn(account);

        service.deposit(new DepositDto("bob", "1111", -75L));

        assertThat(account.getBalance()).isEqualTo(0L);
        verify(repository, times(1)).findByName("bob");
        verify(validator, times(1)).validatePin("1111");
        verify(validator, times(1)).validateBalance(-75L);
    }

    @Test(expectedExceptions = AccountException.class)
    public void deposit_pinNotValid_throwException() {
        Account account = account("bob", 0L);
        when(repository.findByName("bob")).thenReturn(account);

        service.deposit(new DepositDto("bob", "2222", 75L));

        assertThat(account.getBalance()).isEqualTo(0L);
        verify(repository, times(1)).findByName("bob");
        verify(validator, times(1)).validatePin("2222");
        verify(validator, times(1)).validateBalance(75L);
    }

    @Test
    public void withdraw_dtoIsValid_withdrewSuccessfully() {
        Account account = account("bob", 100L);
        when(repository.findByName("bob")).thenReturn(account);

        service.withdraw(new DepositDto("bob", "1111", 25L));

        assertThat(account.getBalance()).isEqualTo(75L);
        verify(repository, times(1)).findByName("bob");
        verify(validator, times(1)).validatePin("1111");
        verify(validator, times(1)).validateBalance(25L);
    }

    @Test(expectedExceptions = AccountException.class)
    public void withdraw_notEnoughMoney_throwException() {
        Account account = account("bob", 100L);
        when(repository.findByName("bob")).thenReturn(account);

        service.withdraw(new DepositDto("bob", "1111", 125L));

        assertThat(account.getBalance()).isEqualTo(100L);
        verify(repository, times(1)).findByName("bob");
        verify(validator, times(1)).validatePin("1111");
        verify(validator, times(1)).validateBalance(125L);
    }

    @Test(expectedExceptions = AccountException.class)
    public void withdraw_pinNotValid_throwException() {
        Account account = account("bob", 100L);
        when(repository.findByName("bob")).thenReturn(account);

        service.withdraw(new DepositDto("bob", "2222", 25L));

        assertThat(account.getBalance()).isEqualTo(100L);
        verify(repository, times(1)).findByName("bob");
        verify(validator, times(1)).validatePin("2222");
        verify(validator, times(1)).validateBalance(25L);
    }

    @Test
    public void transfer_transferDtoIsValid_transferredSuccessfully() {
        Account sourceAccount = account("bob", 100L);
        Account destinationAccount = account("mak", 100L);
        when(repository.findByName("bob")).thenReturn(sourceAccount);
        when(repository.findByName("mak")).thenReturn(destinationAccount);

        service.transfer(new TransferDto(
                "mak", "1111",
                25L,
                "bob", "1111"));

        assertThat(sourceAccount.getBalance()).isEqualTo(75L);
        assertThat(destinationAccount.getBalance()).isEqualTo(125L);
        verify(repository, times(1)).findByName("bob");
        verify(repository, times(1)).findByName("mak");
        verify(validator, times(2)).validatePin("1111");
        verify(validator, times(1)).validateBalance(25L);
        verify(validator, times(1)).validateOnEnoughMoney(25L, 100L);
    }

    @Test(expectedExceptions = AccountException.class)
    public void transfer_notEnoughMoneyOnSourceAcc_throwException() {
        Account sourceAccount = account("bob", 100L);
        Account destinationAccount = account("mak", 100L);
        when(repository.findByName("bob")).thenReturn(sourceAccount);
        when(repository.findByName("mak")).thenReturn(destinationAccount);

        service.transfer(new TransferDto(
                "mak", "1111",
                225L,
                "bob", "1111"));

        assertThat(sourceAccount.getBalance()).isEqualTo(100L);
        assertThat(destinationAccount.getBalance()).isEqualTo(100L);
        verify(repository, times(1)).findByName("bob");
        verify(repository, times(1)).findByName("mak");
        verify(validator, times(2)).validatePin("1111");
        verify(validator, times(1)).validateBalance(25L);
        verify(validator, times(1)).validateOnEnoughMoney(225L, 100L);
    }

    @Test(expectedExceptions = AccountException.class)
    public void transfer_destinationAccPinIsInvalid_throwException() {
        Account sourceAccount = account("bob", 100L);
        Account destinationAccount = account("mak", 100L);
        when(repository.findByName("bob")).thenReturn(sourceAccount);
        when(repository.findByName("mak")).thenReturn(destinationAccount);

        service.transfer(new TransferDto(
                "mak", "2222",
                25L,
                "bob", "1111"));

        assertThat(sourceAccount.getBalance()).isEqualTo(100L);
        assertThat(destinationAccount.getBalance()).isEqualTo(100L);
        verify(repository, times(1)).findByName("bob");
        verify(repository, times(1)).findByName("mak");
        verify(validator, times(1)).validatePin("1111");
        verify(validator, times(1)).validatePin("2222");
        verify(validator, times(1)).validateBalance(25L);
        verify(validator, times(1)).validateOnEnoughMoney(25L, 100L);
    }

    @Test
    public void findAll_returnExpectedListSize() {
        when(repository.findAll()).thenReturn(List.of(account("bob", 25L), account("mak", 35L)));

        List<ShowAccountDto> result = service.findAll();

        assertThat(result.size()).isEqualTo(2);
        verify(repository, times(1)).findAll();
    }


    private Account account(String name, Long balance) {
        Account account = new Account();
        account.setId(1L);
        account.setPin("1111");
        account.setBalance(balance);
        account.setName(name);
        account.setAccountNumber(555555);

        return account;
    }
}
