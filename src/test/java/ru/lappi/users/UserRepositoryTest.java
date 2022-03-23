package ru.lappi.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.lappi.users.entity.Privilege;
import ru.lappi.users.entity.Role;
import ru.lappi.users.entity.User;
import ru.lappi.users.entity.projection.ObjectId;
import ru.lappi.users.repository.UserRepository;
import ru.lappi.users.service.TestObjectsCreator;

import java.util.Optional;
import java.util.Set;
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
    @Autowired
    private TestObjectsCreator testObjectsCreator;

    private final static String USERNAME = UUID.randomUUID().toString();
    private final static String PASSWORD = UUID.randomUUID().toString();

    @BeforeEach
    void init() {
        testObjectsCreator.createRandomUserWithRoles(USERNAME, PASSWORD);
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

    @Test
    void testGetUserPrivilegesUniqueValues() {
        /* Тестовые роль и привилегия */
        Privilege privilege = testObjectsCreator.createAndSaveRandomPrivilege();
        Role role = testObjectsCreator.createAndSaveRandomRole(privilege);

        assertEquals(role.getPrivileges().size(), 1);
        assertEquals(privilege.getCode(), role.getPrivileges().get(0).getCode());

        /* Назначаем пользователю роль и привилегию, которая также есть в роли */
        String username = UUID.randomUUID().toString();
        User user = testObjectsCreator.createRandomUserWithoutRoles(username, username);
        user.getPrivileges().add(privilege);
        user.getRoles().add(role);

        userRepository.save(user);
        user = userRepository.findByUsername(username).orElseThrow(RuntimeException::new);

        /* Список должен содержать только одну запись */
        Set<String> privilegeCodes = user.getAllPrivilegeCodes();
        assertEquals(privilegeCodes.size(), 1);
        assertTrue(privilegeCodes.contains(privilege.getCode()));
    }
}
