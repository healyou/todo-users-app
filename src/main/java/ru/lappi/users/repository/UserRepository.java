package ru.lappi.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lappi.users.entity.User;

/**
 * @author Nikita Gorodilov
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
