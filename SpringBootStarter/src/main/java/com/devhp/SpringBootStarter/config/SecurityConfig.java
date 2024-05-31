package com.devhp.SpringBootStarter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;
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
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
        // Đối với thằng này thì phục vụ chống tấn công cho trường hợp hacker sử cookies của web sử dụng ở một web khác.
        // Nếu API chỉ phục vụ cho Mobile thì không cần vì Mobile thường sử dụng token để xác thực chứ không dùng cookie.
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/v1/register").permitAll()
                        .requestMatchers("/v1/greeting").authenticated()
        )
        // Trong trường hợp mặc định thì thật chất 2 thằng này đã có rồi không cần khai báo, tuy nhiên nếu trong trường hợp disable thì sẽ có sự khác biệt
        // Nêu muốn disable việc xác thực thì chỉ cần disable httpBasic và trong trường hợp này chúng ta vẫn có thể truy cập vào đường dẫn login và hiện form mặc định chỉ
        // là không đăng nhập được.
        // Nếu chúng ta disable luôn thằng formLogin thì khi vào trang nó sẽ báo là access denined
        .formLogin(withDefaults())
        .httpBasic(withDefaults());;

        return httpSecurity.build();
    }

}
