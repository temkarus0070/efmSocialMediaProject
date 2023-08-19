package org.temkarus0070.efmsocialmedia.security.services;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.temkarus0070.efmsocialmedia.security.dto.JwtAuthDto;
import org.temkarus0070.efmsocialmedia.security.exceptions.UserAlreadyRegistratedException;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.AuthToken;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.AuthTokenId;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.UserAccount;
import org.temkarus0070.efmsocialmedia.security.persistence.repository.TokenRepository;
import org.temkarus0070.efmsocialmedia.security.persistence.repository.UserAccountRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegistrationService {

    private JwtEncoder jwtEncoder;
    private UserAccountRepository userAccountRepository;

    private TokenRepository tokenRepository;

    private PasswordEncoder passwordEncoder;

    @Transactional
    public AuthToken registrateUser(UserAccount userAccount) {
        if (!userAccountRepository.existsUsersByEmailOrUsername(userAccount.getEmail(), userAccount.getUsername())) {
            userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
            userAccountRepository.save(userAccount);
            return registrateToken(userAccount.getUsername());
        } else {
            throw new UserAlreadyRegistratedException("пользователь с таким именем или почтой уже зарегистрирован");
        }
    }

    @Transactional
    public AuthToken registrateToken(String username) {
        Jwt token = generateJwt(username);
        Jwt refreshToken = generateRefreshJwt(username);
        AuthToken authToken =
                new AuthToken(new AuthTokenId(token.getTokenValue(), refreshToken.getTokenValue()), new UserAccount(username));
        tokenRepository.save(authToken);
        return authToken;

    }

    private Jwt generateJwt(String username) {
        JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256)
                                       .build();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                                                .expiresAt(Instant.ofEpochSecond((LocalDateTime.now()
                                                                                               .plus(1, ChronoUnit.HOURS)
                                                                                               .toEpochSecond(ZoneOffset.UTC))))
                                                .claim("sub", username)
                                                .claim("scope", "user")
                                                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, jwtClaimsSet));
    }

    private Jwt generateRefreshJwt(String username) {
        JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256)
                                       .build();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                                                .claim("sub", username)

                                                .claim("scope", "user")
                                                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, jwtClaimsSet));
    }

    @Transactional
    public AuthToken refreshToken(JwtAuthDto jwt) {
        Optional<AuthToken> tokenOptional = tokenRepository.findById(new AuthTokenId(jwt.getToken(), jwt.getRefreshToken()));
        if (tokenOptional.isPresent()) {
            AuthToken oldAuthToken = tokenOptional.get();
            Jwt token = generateJwt(oldAuthToken.getUserAccount()
                    .getUsername());
            Jwt refreshToken = generateRefreshJwt(oldAuthToken.getUserAccount()
                    .getUsername());
            AuthToken newToken =
                    new AuthToken(new AuthTokenId(token.getTokenValue(), refreshToken.getTokenValue()), oldAuthToken.getUserAccount());
            tokenRepository.save(newToken);
            tokenRepository.deleteById(oldAuthToken.getId());
            return newToken;
        } else {
            throw new InvalidBearerTokenException("Неверное значение токена авторизации");
        }
    }
}
