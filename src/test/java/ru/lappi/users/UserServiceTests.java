package ru.lappi.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.event.annotation.BeforeTestExecution;
import ru.lappi.users.entity.User;
import ru.lappi.users.repository.UserRepository;
import ru.lappi.users.service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nikita Gorodilov
 */
@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final static String USERNAME = "test";
    private final static String PASSWORD = "test";

    @BeforeTestExecution
    void init() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setPasswordHash(PASSWORD);
        userRepository.save(user);
    }

    @Test
    void testRegisterSuccessful() {
        assertDoesNotThrow(() -> userService.register(USERNAME, PASSWORD));
    }

    @Test
    void testLoginSuccessful() {
        Boolean login = assertDoesNotThrow(() -> userService.login(USERNAME, bCryptPasswordEncoder.encode(PASSWORD)));
        assertEquals(true, login);
    }
}
