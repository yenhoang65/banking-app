package com.example.banking_app.Repository;

import com.example.banking_app.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account, Long> {
}
