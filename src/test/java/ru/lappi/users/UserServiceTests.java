package ru.lappi.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.lappi.users.entity.User;
import ru.lappi.users.repository.RoleRepository;
import ru.lappi.users.repository.UserRepository;
import ru.lappi.users.service.TestObjectsCreator;
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
    @Autowired
    private TestObjectsCreator testObjectsCreator;

    private final static String USERNAME = UUID.randomUUID().toString();
    private final static String PASSWORD = UUID.randomUUID().toString();

    @BeforeEach
    void init() {
        testObjectsCreator.createRandomUserWithRoles(USERNAME, PASSWORD);
    }

    @Test
    void testRegisterSuccessful() {
        assertDoesNotThrow(() -> userService.register(UUID.randomUUID().toString(), PASSWORD));
    }

    @Test
    void testRegisterAddDefaultRole() {
        String username = UUID.randomUUID().toString();
        userService.register(username, PASSWORD);
        User user = userRepository.findByUsername(username).orElseThrow(RuntimeException::new);
        boolean hasDefaultRole = user.getRoles().stream()
                .filter(it -> it.getCode().equals(RoleRepository.ROLE_USER_CODE))
                .count() == 1;
        assertTrue(hasDefaultRole);
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

    @Test
    void testGetUserByIdSuccessful() {
        Optional<Long> userId = userService.getUserId(USERNAME);
        assertTrue(userId.isPresent());
        Long id = userId.get();
        assertNotNull(id);

        Optional<User> user = assertDoesNotThrow(() -> userService.getUserById(id));
        assertTrue(user.isPresent());
    }

    @Test
    void testGetUserByIdNotFound() {
        Optional<User> user = assertDoesNotThrow(() -> userService.getUserById(-1L));
        assertFalse(user.isPresent());
    }
}
