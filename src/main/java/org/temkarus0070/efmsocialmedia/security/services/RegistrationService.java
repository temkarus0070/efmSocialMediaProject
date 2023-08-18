package org.temkarus0070.efmsocialmedia.security.services;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.temkarus0070.efmsocialmedia.security.persistence.dto.JwtAuthDto;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.AuthToken;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.AuthTokenId;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.User;
import org.temkarus0070.efmsocialmedia.security.persistence.exceptions.UserAlreadyRegistratedException;
import org.temkarus0070.efmsocialmedia.security.persistence.repository.TokenRepository;
import org.temkarus0070.efmsocialmedia.security.persistence.repository.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RegistrationService {

    private JwtEncoder jwtEncoder;
    private UserRepository userRepository;

    private TokenRepository tokenRepository;

    private PasswordEncoder passwordEncoder;

    @Transactional
    public AuthToken registrateUser(User user) {
        if (!userRepository.existsUsersByEmailOrUsername(user.getEmail(), user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return registrateToken(user.getUsername());
        } else {
            throw new UserAlreadyRegistratedException("пользователь с таким именем или почтой уже зарегистрирован");
        }
    }

    @Transactional
    public AuthToken registrateToken(String username) {
        Jwt token = generateJwt(username);
        Jwt refreshToken = generateRefreshJwt(username);
        AuthToken authToken =
            new AuthToken(new AuthTokenId(token.getTokenValue(), refreshToken.getTokenValue()), new User(username));
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
            Jwt token = generateJwt(oldAuthToken.getUser()
                                                .getUsername());
            Jwt refreshToken = generateRefreshJwt(oldAuthToken.getUser()
                                                              .getUsername());
            AuthToken newToken =
                new AuthToken(new AuthTokenId(token.getTokenValue(), refreshToken.getTokenValue()), oldAuthToken.getUser());
            tokenRepository.save(newToken);
            tokenRepository.deleteById(oldAuthToken.getId());
            return newToken;
        } else {
            throw new InvalidBearerTokenException("Неверное значение токена авторизации");
        }
    }
}
