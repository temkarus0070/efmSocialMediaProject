package org.temkarus0070.efmsocialmedia.swagger;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecuritySchemes(value = {@SecurityScheme(name = "form-auth", in = SecuritySchemeIn.DEFAULT, type = SecuritySchemeType.OAUTH2,
                                          description = "Используется для получения токена авторизации и токена для обновления авторизации"),
    @SecurityScheme(name = "jwt", in = SecuritySchemeIn.HEADER, type = SecuritySchemeType.APIKEY,
                    description = "Основной вид авторизации используемый в приложении")})
public class SwaggerConfig {}
