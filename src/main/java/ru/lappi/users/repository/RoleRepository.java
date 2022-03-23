package ru.lappi.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lappi.users.entity.Role;

import java.util.Optional;

/**
 * @author Nikita Gorodilov
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    String ROLE_USER_CODE = "ROLE_USER";

    Optional<Role> findByCode(String code);
}
