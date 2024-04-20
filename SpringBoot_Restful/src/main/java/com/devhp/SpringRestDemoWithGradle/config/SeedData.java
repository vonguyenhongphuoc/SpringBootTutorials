package com.devhp.SpringRestDemoWithGradle.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.devhp.SpringRestDemoWithGradle.model.Account;
import com.devhp.SpringRestDemoWithGradle.service.AccountService;
import com.devhp.SpringRestDemoWithGradle.util.constants.Authority;

@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    private AccountService accountService;

    @Override
    public void run(String... args) throws Exception {
        Account account01 = new Account();
        Account account02 = new Account();

        account01.setEmail("user@user.com");
        account01.setPassword("pass987");
        account01.setAuthorities(Authority.USER.toString());
        accountService.save(account01);

        account02.setEmail("admin@admin.com");
        account02.setPassword("pass987");
        account02.setAuthorities(Authority.ADMIN+" "+Authority.USER);;
        accountService.save(account02);

    }
    
    
}
