package com.example.banking_app.Mapper;

import com.example.banking_app.Dto.AccountDTO;
import com.example.banking_app.Entity.Account;

public class AccountMapper {
    public static Account mapToAccount(AccountDTO accountDto){
        Account account = new Account(
                accountDto.getId(),
                accountDto.getAccountHolderName(),
                accountDto.getBalance()
        );

        return account;
    }

    public static AccountDTO mapToAccountDto(Account account){
        AccountDTO accountDto = new AccountDTO(
                account.getId(),
                account.getAccountHolderName(),
                account.getBalance()
        );
        return accountDto;
    }
}
