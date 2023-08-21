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
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.UserAccount;
import org.temkarus0070.efmsocialmedia.security.persistence.repository.UserAccountRepository;

import java.util.Optional;

@Component
public class JwtAuthManager implements AuthenticationProvider {

    private UserAccountRepository userAccountRepository;
    private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter = new JwtAuthenticationConverter();

    private JwtDecoder jwtDecoder;


    public JwtAuthManager(UserAccountRepository userAccountRepository, JwtDecoder jwtDecoder) {
        this.userAccountRepository = userAccountRepository;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        BearerTokenAuthenticationToken token = (BearerTokenAuthenticationToken) authentication;
            Jwt decode = jwtDecoder.decode(token.getToken());
            String username = (String) decode.getClaimAsString("sub");
            Optional<UserAccount> userOptional = userAccountRepository.findById(username);
            if (userOptional.isPresent()) {
                UserAccount userAccount = userOptional.get();
                if (userAccount.isEnabled()) {
                    AbstractAuthenticationToken convert = jwtAuthenticationConverter.convert(decode);
                    return convert;
                } else {
                    throw new DisabledException("Ваш аккаунт был заблокирован");
                }
            } else {
                throw new UsernameNotFoundException("Пользователь с таким токеном не зарегистрирован");
            }

    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(BearerTokenAuthenticationToken.class);
    }
}
