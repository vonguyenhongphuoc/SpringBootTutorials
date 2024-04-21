package com.devhp.SpringRestDemoWithGradle.controller;

import java.lang.StackWalker.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.devhp.SpringRestDemoWithGradle.model.Account;
import com.devhp.SpringRestDemoWithGradle.payload.auth.AccountDTO;
import com.devhp.SpringRestDemoWithGradle.payload.auth.AccountViewDTO;
import com.devhp.SpringRestDemoWithGradle.payload.auth.PasswordDTO;
import com.devhp.SpringRestDemoWithGradle.payload.auth.ProfileDTO;
import com.devhp.SpringRestDemoWithGradle.payload.auth.TokenDTO;
import com.devhp.SpringRestDemoWithGradle.payload.auth.UserLoginDTO;
import com.devhp.SpringRestDemoWithGradle.service.AccountService;
import com.devhp.SpringRestDemoWithGradle.service.TokenService;
import com.devhp.SpringRestDemoWithGradle.util.constants.AccountError;
import com.devhp.SpringRestDemoWithGradle.util.constants.AccountSuccess;
import com.devhp.SpringRestDemoWithGradle.util.constants.Constants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Controller", description = "Controller for Account management")
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AccountService accountService;

    @PostMapping(value = "/token", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TokenDTO> token(@Valid @RequestBody UserLoginDTO userLogin) throws AuthenticationException {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword()));
            return ResponseEntity.ok(new TokenDTO(tokenService.generateToken(authentication)));
        } catch (Exception e) {
            log.debug(AccountError.TOKEN_GENERATION_ERROR.toString() + ": " + e.getMessage());
            return new ResponseEntity<>(new TokenDTO(null), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = "/users/add")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponse(responseCode = "400", description = "Please enter a valid email and Password length between 6 to 20 characters")
    @ApiResponse(responseCode = "201", description = "Account added")
    @Operation(summary = "Add a new User")
    public ResponseEntity<String> addUser(@Valid @RequestBody AccountDTO accountDTO) {
        try {
            Account account = new Account();
            account.setEmail(accountDTO.getEmail());
            account.setPassword(accountDTO.getPassword());
            accountService.save(account);
            return ResponseEntity.status(HttpStatus.CREATED).body(AccountSuccess.ACCOUNT_ADDED.toString());
        } catch (Exception e) {
            log.debug(AccountError.ADD_ACCOUNT_ERROR + ": " + e.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping(value = "/users", produces = "application/json")
    @SecurityRequirement(name = Constants.SECURITY_APP_NAME)
    public List<AccountViewDTO> users() {
        List<AccountViewDTO> accounts = new ArrayList<>();

        for (Account account : accountService.findAll()) {
            accounts.add(new AccountViewDTO(account.getId(), account.getEmail(), account.getAuthorities()));
        }
        return accounts;
    }

    @GetMapping(value = "/profile", produces = "application/json")
    @SecurityRequirement(name = Constants.SECURITY_APP_NAME)
    public ProfileDTO profile(Authentication authentication) {
        if (authentication != null) {
            String email = authentication.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(email);
            if (optionalAccount.isPresent()) {
                Account account = optionalAccount.get();
                ProfileDTO profileDTO = new ProfileDTO(account.getId(), account.getEmail(), account.getAuthorities());
                return profileDTO;
            }
            return null;
        }

        return new ProfileDTO(0, "", "");
    }

    @PostMapping(value = "/profile/update-password", consumes = "application/json" ,produces = "application/json")
    @SecurityRequirement(name = Constants.SECURITY_APP_NAME)
    public AccountViewDTO updatePassword(@Valid @RequestBody PasswordDTO passwordDTO, Authentication authentication) {
        if (authentication != null) {
            String email = authentication.getName();
            Optional<Account> optionalAccount = accountService.findByEmail(email);
            if (optionalAccount.isPresent()) {
                Account account = optionalAccount.get();
                account.setPassword(passwordDTO.getPassword());
                accountService.save(account); 
                AccountViewDTO accountViewDTO = new AccountViewDTO(account.getId(), account.getEmail(), account.getAuthorities());
                return accountViewDTO;
            }
            return null;
        }

        return null;
    }
}
