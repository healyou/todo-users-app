package ru.lappi.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lappi.users.entity.User;
import ru.lappi.users.entity.projection.ObjectId;

import java.util.Optional;

/**
 * @author Nikita Gorodilov
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<ObjectId> findUserIdByUsername(String username);
}
