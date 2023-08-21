package org.temkarus0070.efmsocialmedia.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.AuthToken;

@Getter
@Setter
@NoArgsConstructor
public class JwtAuthDto {

    @NotNull(message = "token не может быть null")
    @NotBlank(message = "token не может быть пустым")
    private String token;
    @NotNull(message = "refresh token не может быть null")
    @NotBlank(message = "refresh token не может быть пустым")
    private String refreshToken;

    public JwtAuthDto(AuthToken authToken) {
        this.token = authToken.getId()
                              .getToken();
        this.refreshToken = authToken.getId()
                                     .getRefreshToken();
    }
}
