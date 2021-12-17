package ru.lappi.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.lappi.users.entity.User;
import ru.lappi.users.repository.UserRepository;
import ru.lappi.users.service.UserServiceImpl;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nikita Gorodilov
 */
public class UserServiceTests extends AbstractTest {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final static String USERNAME = UUID.randomUUID().toString();
    private final static String PASSWORD = UUID.randomUUID().toString();

    @BeforeEach
    void init() {
        User user = new User();
        user.setUsername(USERNAME);
        user.setPasswordHash(bCryptPasswordEncoder.encode(PASSWORD));
        userRepository.save(user);
    }

    @Test
    void testRegisterSuccessful() {
        assertDoesNotThrow(() -> userService.register(UUID.randomUUID().toString(), PASSWORD));
    }

    @Test
    void testRegisterSameUsername() {
        assertThrows(DataIntegrityViolationException.class, () -> userService.register(USERNAME, PASSWORD));
    }

    @Test
    void testLoginSuccessful() {
        Boolean login = assertDoesNotThrow(() -> userService.login(USERNAME, PASSWORD));
        assertEquals(true, login);
    }

    @Test
    void testLoginWrongPassword() {
        Boolean login = assertDoesNotThrow(() -> userService.login(USERNAME, UUID.randomUUID().toString()));
        assertEquals(false, login);
    }

    @Test
    void testLoginWrongLogin() {
        Boolean login = assertDoesNotThrow(() -> userService.login(UUID.randomUUID().toString(), PASSWORD));
        assertEquals(false, login);
    }

    @Test
    void testGetUserIdBySuccessful() {
        Optional<Long> login = assertDoesNotThrow(() -> userService.getUserId(USERNAME));
        assertTrue(login.isPresent());
        assertNotNull(login.get());
    }

    @Test
    void testGetUserIdByNotFound() {
        Optional<Long> login = assertDoesNotThrow(() -> userService.getUserId(UUID.randomUUID().toString()));
        assertFalse(login.isPresent());
    }
}
