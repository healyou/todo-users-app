package ru.lappi.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.lappi.users.entity.User;
import ru.lappi.users.entity.projection.ObjectId;
import ru.lappi.users.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Nikita Gorodilov
 */
public class UserRepositoryTest extends AbstractTest {
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
    void testFindUserByUsernameSuccessful() {
        Optional<User> user = assertDoesNotThrow(() -> userRepository.findByUsername(USERNAME));
        assertTrue(user.isPresent());
    }

    @Test
    void testFindUserByUsernameNotFound() {
        Optional<User> user = assertDoesNotThrow(() -> userRepository.findByUsername(UUID.randomUUID().toString()));
        assertFalse(user.isPresent());
    }

    @Test
    void testFindUserIdByUsernameSuccessful() {
        Optional<ObjectId> userId = assertDoesNotThrow(() -> userRepository.findUserIdByUsername(USERNAME));
        assertTrue(userId.isPresent());
        assertNotNull(userId.get());
    }

    @Test
    void testFindUserIdByUsernameNotFound() {
        Optional<ObjectId> userId = assertDoesNotThrow(() -> userRepository.findUserIdByUsername(UUID.randomUUID().toString()));
        assertFalse(userId.isPresent());
    }
}
