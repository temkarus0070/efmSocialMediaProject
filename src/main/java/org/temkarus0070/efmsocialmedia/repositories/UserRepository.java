package org.temkarus0070.efmsocialmedia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.temkarus0070.efmsocialmedia.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
