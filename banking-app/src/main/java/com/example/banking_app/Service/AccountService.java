package com.example.banking_app.Service;

import com.example.banking_app.Dto.AccountDTO;
import com.example.banking_app.Dto.TransferFunDto;

import java.util.List;

public interface AccountService {

    AccountDTO createAccount(AccountDTO accountDTO);

    AccountDTO getAccountById(Long id);

    AccountDTO deposit(Long id, double amount);

    AccountDTO withdraw(Long id, double amount);

    List<AccountDTO> getAllAccounts();

    void deleteAccount(Long id);

    void transferFunds(TransferFunDto transferFunDto);
}
