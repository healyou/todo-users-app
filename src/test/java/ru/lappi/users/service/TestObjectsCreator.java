package ru.lappi.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lappi.users.entity.Privilege;
import ru.lappi.users.entity.Role;
import ru.lappi.users.entity.User;
import ru.lappi.users.repository.PrivilegeRepository;
import ru.lappi.users.repository.RoleRepository;
import ru.lappi.users.repository.UserRepository;

import java.util.Collections;
import java.util.UUID;

/**
 * @author Nikita Gorodilov
 */
@Service
@Transactional
public class TestObjectsCreator {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PrivilegeRepository privilegeRepository;

    public void createRandomUserWithRoles(String username, String password) {
        Privilege privilege = createAndSaveRandomPrivilege();
        Role role = createAndSaveRandomRole(privilege);

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(bCryptPasswordEncoder.encode(password));
        user.setRoles(Collections.singletonList(role));
        user.setPrivileges(Collections.singletonList(createAndSaveRandomPrivilege()));

        userRepository.save(user);
    }

    public User createRandomUserWithoutRoles(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(bCryptPasswordEncoder.encode(password));

        userRepository.save(user);
        return user;
    }

    public Role createAndSaveRandomRole(Privilege privilege) {
        String code = UUID.randomUUID().toString();

        Role role = new Role();
        role.setCode(code);
        role.setDescription(code);
        role.setPrivileges(Collections.singletonList(privilege));

        roleRepository.save(role);

        return role;
    }

    public Privilege createAndSaveRandomPrivilege() {
        String code = UUID.randomUUID().toString();

        Privilege privilege = new Privilege();
        privilege.setCode(code);
        privilege.setDescription(code);

        privilegeRepository.save(privilege);

        return privilege;
    }
}
