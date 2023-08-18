package org.temkarus0070.efmsocialmedia.security.services;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.User;
import org.temkarus0070.efmsocialmedia.security.persistence.repository.UserRepository;

import java.util.Optional;

@Component
public class JwtAuthManager implements AuthenticationProvider {

    private UserRepository userRepository;
    private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter = new JwtAuthenticationConverter();

    private JwtDecoder jwtDecoder;


    public JwtAuthManager(UserRepository userRepository, JwtDecoder jwtDecoder) {
        this.userRepository = userRepository;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        BearerTokenAuthenticationToken token = (BearerTokenAuthenticationToken) authentication;
        try {
            Jwt decode = jwtDecoder.decode(token.getToken());
            String username = (String) decode.getClaimAsString("sub");
            Optional<User> userOptional = userRepository.findByUsername(username);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (user.isEnabled()) {
                    AbstractAuthenticationToken convert = jwtAuthenticationConverter.convert(decode);
                    return convert;
                } else {
                    throw new DisabledException("Ваш аккаунт был заблокирован");
                }
            } else {
                throw new UsernameNotFoundException("Пользователь с таким токеном не зарегистрирован");
            }
        } catch (JwtException jwtException) {
            throw new InvalidBearerTokenException("Неверное значение токена авторизации");
        }

        //            if (jwtException.getErrors().stream().anyMatch(e->e.getDescription().contains("Jwt expired at"))) {
        //
        //            }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(BearerTokenAuthenticationToken.class);
    }
}
