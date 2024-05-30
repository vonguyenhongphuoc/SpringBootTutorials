package com.devhp.SpringBootStarter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    // @Bean
    // public UserDetailsService userDetailsService(DataSource dataSource) {
    //     UserDetails user = User.builder().username("itkuiuser").password("$2a$10$I2cNFJrHTd.G7FEWwtgBD.SxhhRCzzCm33QAFLFJJAlOu7zW13tC2").roles("USER").build();
    //     UserDetails admin = User.builder().username("itkuiadmin").password("$2a$10$I2cNFJrHTd.G7FEWwtgBD.SxhhRCzzCm33QAFLFJJAlOu7zW13tC2").roles("USER", "ADMIN").build();

    //     JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
    //     users.createUser(user);
    //     users.createUser(admin);
    //     return users;
    // }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
