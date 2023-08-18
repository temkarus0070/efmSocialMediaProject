package org.temkarus0070.efmsocialmedia.security.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.AuthToken;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.AuthTokenId;

@Repository
public interface TokenRepository extends JpaRepository<AuthToken, AuthTokenId> {}
