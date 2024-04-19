package com.devhp.SpringRestDemoWithGradle.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devhp.SpringRestDemoWithGradle.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
}
