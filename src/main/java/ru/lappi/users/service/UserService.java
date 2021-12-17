package ru.lappi.users.service;

import java.util.Optional;

/**
 * @author Nikita Gorodilov
 */
public interface UserService {

    void register(String username, String password);

    boolean login(String username, String password);

    Optional<Long> getUserId(String username);
}
