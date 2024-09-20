package com.example.banking_app.Dto;

public record TransferFunDto(Long fromAccountId,
                              Long toAccountId,
                              double amount) {
}