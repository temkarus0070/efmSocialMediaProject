package org.temkarus0070.efmsocialmedia.security.persistence.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.AuthToken;

@Getter
@Setter
@NoArgsConstructor
public class JwtAuthDto {

    private String token;
    private String refreshToken;

    public JwtAuthDto(AuthToken authToken) {
        this.token = authToken.getId()
                              .getToken();
        this.refreshToken = authToken.getId()
                                     .getRefreshToken();
    }
}
