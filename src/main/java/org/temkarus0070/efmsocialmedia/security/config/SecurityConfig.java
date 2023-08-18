package org.temkarus0070.efmsocialmedia.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.temkarus0070.efmsocialmedia.security.dto.ErrorDto;
import org.temkarus0070.efmsocialmedia.security.dto.JwtAuthDto;
import org.temkarus0070.efmsocialmedia.security.services.JwtAuthManager;
import org.temkarus0070.efmsocialmedia.security.services.RegistrationService;

import java.io.IOException;

@Configuration
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

                options.requestMatchers("/auth/registrate", "/auth/refresh-token", "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html")
                       .permitAll()
                       .anyRequest()
                       .authenticated();
            })
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
                exceptions.accessDeniedHandler(this::writeExceptionWhenBadRequest);

                exceptions.authenticationEntryPoint(this::writeExceptionWhenBadRequest);
            })
            .oauth2ResourceServer((resourceServer) -> resourceServer.jwt(Customizer.withDefaults()))
            .authenticationManager(new ProviderManager(jwtAuthManager,
                                                       daoAuthenticationProvider(userDetailsService, passwordEncoder)));

        return http.build();
    }

    private void writeExceptionWhenBadRequest(HttpServletRequest request, HttpServletResponse response, Exception authException)
        throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(400);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), new ErrorDto(authException.getLocalizedMessage()));
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
