package com.devhp.SpringBootStarter.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.devhp.SpringBootStarter.model.Customer;
import com.devhp.SpringBootStarter.repository.CustomerRepository;

@Component
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider{

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        List<Customer> customers = customerRepository.findByUsername(username); 
        if(org.springframework.util.CollectionUtils.isEmpty(customers)){
            throw new BadCredentialsException("No customer registered with this username="+username);
        }else{
            if(passwordEncoder.matches(password, customers.get(0).getPassword())){
               List<GrantedAuthority> authorities = new ArrayList<>();
               authorities.add(new SimpleGrantedAuthority(customers.get(0).getRole()));
               return new UsernamePasswordAuthenticationToken(username, password, authorities);
            }else {
                throw new BadCredentialsException("Invalid password for username="+username);
            }
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
      	return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
    
}
