package ru.lappi.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.lappi.users.entity.User;
import ru.lappi.users.repository.UserRepository;

/**
 * @author Nikita Gorodilov
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void register(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(bCryptPasswordEncoder.encode(password));

        userRepository.save(user);
    }

    @Override
    public boolean login(String username, String passwordHash) {
        ExampleMatcher userMatcher = ExampleMatcher.matching()
                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withMatcher("password_hash", ExampleMatcher.GenericPropertyMatchers.exact());

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(passwordHash);

        Example<User> example = Example.of(user, userMatcher);
        return userRepository.exists(example);
    }
}
