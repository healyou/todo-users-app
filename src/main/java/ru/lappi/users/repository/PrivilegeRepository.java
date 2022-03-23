package ru.lappi.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.lappi.users.entity.Privilege;

/**
 * @author Nikita Gorodilov
 */
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
}
