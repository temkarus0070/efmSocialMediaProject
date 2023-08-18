package org.temkarus0070.efmsocialmedia.security.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsUsersByEmailOrUsername(String email, String username);

    Optional<User> findByUsername(String username);
}
