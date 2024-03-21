package com.devhp.SpringBootStarter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devhp.SpringBootStarter.models.Account;
import com.devhp.SpringBootStarter.repositories.AccountRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    
    public Account save(Account account){
        return accountRepository.save(account);
    }
    
}
