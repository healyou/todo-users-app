package ru.lappi.users;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

/**
 * @author Nikita Gorodilov
 */
@Configuration
public class UsersConfiguration {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ApplicationRunner runner(DataSource dataSource) {
        /* check ds connection is success */
        return args -> dataSource.getConnection();
    }
}
