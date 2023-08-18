package org.temkarus0070.efmsocialmedia.swagger;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
    @SecurityScheme(name = "jwt", in = SecuritySchemeIn.HEADER, type = SecuritySchemeType.APIKEY,
                    description = "Основной вид авторизации используемый в приложении", bearerFormat = "jwt")
public class SwaggerConfig {}
