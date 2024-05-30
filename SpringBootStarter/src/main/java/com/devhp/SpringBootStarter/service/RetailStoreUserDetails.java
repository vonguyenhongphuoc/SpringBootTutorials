package com.devhp.SpringBootStarter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.devhp.SpringBootStarter.model.Customer;
import com.devhp.SpringBootStarter.repository.CustomerRepository;

@Service
public class RetailStoreUserDetails implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Customer> customers =  customerRepository.findByUsername(username);
        String password = null;
        List<GrantedAuthority> authorities = null;
        if(customers.isEmpty()){
            throw new UsernameNotFoundException("User details not found for username = " + username);
        }
        username = customers.get(0).getUsername();
        password = customers.get(0).getPassword();
        authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(customers.get(0).getRole()));


        return new User(username, password, authorities);
    }
    
    
}
