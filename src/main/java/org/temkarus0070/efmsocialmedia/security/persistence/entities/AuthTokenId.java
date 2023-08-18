package org.temkarus0070.efmsocialmedia.security.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthTokenId implements Serializable {

    @Column(length = 1024)
    private String token;

    @Column(length = 1024)
    private String refreshToken;

}
