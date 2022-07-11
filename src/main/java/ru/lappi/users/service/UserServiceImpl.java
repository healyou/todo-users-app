package ru.lappi.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lappi.users.entity.Role;
import ru.lappi.users.entity.User;
import ru.lappi.users.entity.projection.ObjectId;
import ru.lappi.users.repository.RoleRepository;
import ru.lappi.users.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

/**
 * @author Nikita Gorodilov
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void register(String username, String password) {
        Optional<Role> defaultUserRole = roleRepository.findByCode(RoleRepository.ROLE_USER_CODE);

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(bCryptPasswordEncoder.encode(password));
        user.setRoles(Collections.singletonList(defaultUserRole.orElseThrow(RuntimeException::new)));

        userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            String encodedPsw = user.get().getPasswordHash();
            return bCryptPasswordEncoder.matches(password, encodedPsw);
        } else {
            return false;
        }
    }

    @Override
    public Optional<Long> getUserId(String username) {
        Optional<ObjectId> objectId = userRepository.findUserIdByUsername(username);
        return objectId.map(ObjectId::getId).or(Optional::empty);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }
}
