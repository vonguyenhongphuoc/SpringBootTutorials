package com.devhp.SpringBootStarter.config;


import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfig {

    @Bean
    DataSource dataSource() {
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/first_db_devhp") // Thay thế your_database bằng tên cơ sở dữ liệu của bạn
                .username("postgres") // Thay thế your_username bằng tên người dùng PostgreSQL của bạn
                .password("P@ssw0rd") // Thay thế your_password bằng mật khẩu PostgreSQL của bạn
                .build();
    }
}
