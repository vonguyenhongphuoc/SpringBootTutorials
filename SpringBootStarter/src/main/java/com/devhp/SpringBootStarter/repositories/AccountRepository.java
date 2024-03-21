package com.devhp.SpringBootStarter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devhp.SpringBootStarter.models.Account;

@Repository
public interface AccountRepository  extends JpaRepository<Account, Long>{
    
}
