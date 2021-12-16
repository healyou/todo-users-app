package ru.lappi.users.service;

/**
 * @author Nikita Gorodilov
 */
public interface UserService {

    void register(String username, String password);

    boolean login(String username, String password);
}
