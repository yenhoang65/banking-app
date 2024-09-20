package com.example.banking_app.Service.Impl;

import com.example.banking_app.Dto.AccountDTO;
import com.example.banking_app.Dto.TransferFunDto;
import com.example.banking_app.Entity.Account;
import com.example.banking_app.Entity.Transaction;
import com.example.banking_app.Exception.AccountException;
import com.example.banking_app.Mapper.AccountMapper;
import com.example.banking_app.Repository.AccountRepo;
import com.example.banking_app.Repository.TransactionRepo;
import com.example.banking_app.Service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepo accountRepo;
    private TransactionRepo transactionRepo;

    private static final String TRANSACTION_TYPE_DEPOSIT = "DEPOSIT";
    private static final String TRANSACTION_TYPE_WITHDRAW = "WITHDRAW";
    private static final String TRANSACTION_TYPE_TRANSFER = "TRANSFER";

    public AccountServiceImpl(AccountRepo accountRepo,
                              TransactionRepo transactionRepo) {
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }

    @Override
    public AccountDTO createAccount(@RequestBody AccountDTO accountDTO) {
        Account account = AccountMapper.mapToAccount(accountDTO);
        Account savedAccount = accountRepo.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDTO getAccountById(@PathVariable Long id) {
        Account account = accountRepo
                .findById(id)
                .orElseThrow(() -> new AccountException("Account does not exists"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDTO deposit(Long id, double amount) {

        Account account = accountRepo
                .findById(id)
                .orElseThrow(() -> new AccountException("Account does not exists"));

        double total = account.getBalance() + amount;
        account.setBalance(total);
        Account savedAccount = accountRepo.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(TRANSACTION_TYPE_DEPOSIT);
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepo.save(transaction);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDTO withdraw(Long id, double amount) {

        Account account = accountRepo
                .findById(id)
                .orElseThrow(() -> new AccountException("Account does not exists"));

        if(account.getBalance() < amount){
            throw new AccountException("Insufficient amount");
        }

        double total = account.getBalance() - amount;
        account.setBalance(total);
        Account savedAccount = accountRepo.save(account);


        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(TRANSACTION_TYPE_WITHDRAW);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepo.save(transaction);

        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepo.findAll();
        return accounts.stream().map((account) -> AccountMapper.mapToAccountDto(account))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAccount(Long id) {

        Account account = accountRepo
                .findById(id)
                .orElseThrow(() -> new AccountException("Account does not exists"));

        accountRepo.deleteById(id);
    }

    @Override
    public void transferFunds(TransferFunDto transferFunDto) {
        // Retrieve the account from which we send the amount
        Account fromAccount = accountRepo
                .findById(transferFunDto.fromAccountId())
                .orElseThrow(() -> new AccountException("Account does not exists"));

        // Retrieve the account to which we send the amount
        Account toAccount = accountRepo.findById(transferFunDto.toAccountId())
                .orElseThrow(() -> new AccountException("Account does not exists"));
        if(fromAccount.getBalance() < transferFunDto.amount()){
            throw new RuntimeException("Insufficient Amount");
        }
        // Debit the amount from fromAccount object
        fromAccount.setBalance(fromAccount.getBalance() - transferFunDto.amount());

        // Credit the amount to toAccount object
        toAccount.setBalance(toAccount.getBalance() + transferFunDto.amount());

        accountRepo.save(fromAccount);

        accountRepo.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setAccountId(transferFunDto.fromAccountId());
        transaction.setAmount(transferFunDto.amount());
        transaction.setTransactionType(TRANSACTION_TYPE_TRANSFER);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepo.save(transaction);
    }
}
