package org.temkarus0070.efmsocialmedia.security.persistence.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Auth_tokens")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthToken {

    @EmbeddedId
    private AuthTokenId id;

    @ManyToOne
    private User user;

}

