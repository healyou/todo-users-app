package ru.lappi.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lappi.users.entity.User;
import ru.lappi.users.repository.UserRepository;

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
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void register(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(bCryptPasswordEncoder.encode(password));

        userRepository.saveAndFlush(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean login(String username, String password) {
        ExampleMatcher userMatcher = ExampleMatcher.matching()
                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.ignoreCase());

        User findUser = new User();
        findUser.setUsername(username);
        Example<User> example = Example.of(findUser, userMatcher);

        Optional<User> user = userRepository.findOne(example);
        if (user.isPresent()) {
            String encodedPsw = user.get().getPasswordHash();
            return bCryptPasswordEncoder.matches(password, encodedPsw);
        } else {
            return false;
        }
    }
}
