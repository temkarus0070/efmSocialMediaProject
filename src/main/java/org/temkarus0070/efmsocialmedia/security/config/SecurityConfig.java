package org.temkarus0070.efmsocialmedia.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.temkarus0070.efmsocialmedia.security.persistence.dto.ErrorDto;
import org.temkarus0070.efmsocialmedia.security.persistence.dto.JwtAuthDto;
import org.temkarus0070.efmsocialmedia.security.services.JwtAuthManager;
import org.temkarus0070.efmsocialmedia.security.services.RegistrationService;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private JwtAuthManager jwtAuthManager;

    private RegistrationService registrationService;

    private UserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;


    @Bean
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
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
            .sessionManagement(AbstractHttpConfigurer::disable)

            .formLogin((options) -> {
                options.successHandler((request, response, authentication) -> {
                    objectMapper.writeValue(response.getWriter(),
                                            new JwtAuthDto(registrationService.registrateToken(authentication.getName())));
                });
            })
            .exceptionHandling((exceptions) -> {
                exceptions.accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(400);
                    response.setCharacterEncoding("UTF-8");
                    objectMapper.writeValue(response.getWriter(), new ErrorDto(accessDeniedException.getLocalizedMessage()));
                });

                exceptions.authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(400);
                    response.setCharacterEncoding("UTF-8");
                    objectMapper.writeValue(response.getWriter(), new ErrorDto(authException.getLocalizedMessage()));
                });
            })
            .oauth2ResourceServer((resourceServer) -> resourceServer.jwt(Customizer.withDefaults()))
            .authenticationManager(new ProviderManager(jwtAuthManager,
                                                       daoAuthenticationProvider(userDetailsService, passwordEncoder)));

        return http.build();
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
