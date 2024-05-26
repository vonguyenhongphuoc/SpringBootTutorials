package com.devhp.SpringBootStarter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder().username("itkuiuser").password("{noop}password1").roles("USER").build();
        UserDetails admin = User.builder().username("itkuiadmin").password("{bcrypt}$2a$10$TixXtxWd.0alJUrwWrHxreResmDxD/jMijcUvIx4tQAaMI.imYUW.").roles("USER", "ADMIN").build();
        return new InMemoryUserDetailsManager(user, admin);
    }

}
