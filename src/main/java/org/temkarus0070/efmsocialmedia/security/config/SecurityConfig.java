package org.temkarus0070.efmsocialmedia.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.temkarus0070.efmsocialmedia.security.dto.ErrorDto;
import org.temkarus0070.efmsocialmedia.security.dto.JwtAuthDto;
import org.temkarus0070.efmsocialmedia.security.services.JwtAuthManager;
import org.temkarus0070.efmsocialmedia.security.services.RegistrationService;

import java.io.IOException;
import java.util.Set;

@Configuration
@EnableTransactionManagement(order = 0)
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private JwtAuthManager jwtAuthManager;

    private RegistrationService registrationService;

    private UserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;

    ObjectMapper objectMapper = new ObjectMapper();

    public SecurityConfig(JwtAuthManager jwtAuthManager,
                          RegistrationService registrationService,
                          UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.jwtAuthManager = jwtAuthManager;
        this.registrationService = registrationService;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(options -> {

                options.requestMatchers("/auth/registrate",
                                        "/auth/refresh-token",
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html")
                       .permitAll()
                       .anyRequest()
                       .authenticated();
            })
            .anonymous(Customizer.withDefaults())
            .sessionManagement(AbstractHttpConfigurer::disable)

            .formLogin((options) -> {
                options.failureHandler((request, response, exception) -> {

                    response.setContentType(MediaType.APPLICATION_JSON.getType());
                    response.setStatus(401);
                    objectMapper.writeValue(response.getWriter(), new ErrorDto(exception.getLocalizedMessage()));
                });
                options.successHandler((request, response, authentication) -> {
                    response.setContentType(MediaType.APPLICATION_JSON.getType());
                    objectMapper.writeValue(response.getWriter(),
                                            new JwtAuthDto(registrationService.registrateToken(authentication.getName())));
                });
            })
            .exceptionHandling((exceptions) -> {
                exceptions.accessDeniedHandler(this::writeExceptionWhenAuthErrors);

                exceptions.authenticationEntryPoint(this::writeExceptionWhenAuthErrors);
            })
            .oauth2ResourceServer((resourceServer) -> resourceServer.jwt(Customizer.withDefaults()))
            .authenticationManager(new ProviderManager(jwtAuthManager,
                                                       daoAuthenticationProvider(userDetailsService, passwordEncoder)));

        return http.build();
    }

    private void writeExceptionWhenAuthErrors(HttpServletRequest request, HttpServletResponse response, Exception authException)
        throws IOException, ServletException {
        Set<String> authUrls = Set.of("/auth/registrate", "/auth/refresh-token", "/login");
        String requestPath = request.getPathInfo();
        if (authUrls.contains(requestPath)) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(401);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), new ErrorDto(authException.getLocalizedMessage()));
        } else if (authException.getClass()
                                .equals(AccessDeniedException.class)) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(403);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), new ErrorDto(authException.getLocalizedMessage()));

        } else if (authException.getClass()
                                .equals(InsufficientAuthenticationException.class)) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(401);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), new ErrorDto(authException.getLocalizedMessage()));
        }

    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(UserDetailsService userDetailsService,
                                                               PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider daoAuthenticationProvider1 = new DaoAuthenticationProvider();
        daoAuthenticationProvider1.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider1.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider1;
    }

}
