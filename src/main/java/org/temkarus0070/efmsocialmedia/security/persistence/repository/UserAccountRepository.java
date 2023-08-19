package org.temkarus0070.efmsocialmedia.security.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

    boolean existsUsersByEmailOrUsername(String email, String username);
}
