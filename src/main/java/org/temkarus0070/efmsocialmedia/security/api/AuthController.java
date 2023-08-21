package org.temkarus0070.efmsocialmedia.security.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.temkarus0070.efmsocialmedia.security.dto.ErrorDto;
import org.temkarus0070.efmsocialmedia.security.dto.JwtAuthDto;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.UserAccount;
import org.temkarus0070.efmsocialmedia.security.services.RegistrationService;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Tag(name = "API для аутентификации/регистрации", description = "Аутентификация, регистрация и все связанное с этим")
public class AuthController {

    private RegistrationService registrationService;

    @PostMapping("/registrate")
    @Operation(operationId = "registrate", description = "регистрация нового пользователя",
               responses = {@ApiResponse(responseCode = "200", description = "Регистрация пользователя прошла успешно"),
                   @ApiResponse(responseCode = "400", description = "неправильный ввод данных для регистрации",
                                content = @Content(mediaType = "application/json",
                                                   schema = @Schema(implementation = ErrorDto.class)))})
    public JwtAuthDto registrate(@Valid @RequestBody UserAccount userAccount) {
        return new JwtAuthDto(registrationService.registrateUser(userAccount));
    }

    @PostMapping("/refresh-token")
    @Operation(operationId = "registrate", description = "обновление jwt токена для авторизации",
               responses = {@ApiResponse(responseCode = "200", description = "Токен успешно обновлен"),
                   @ApiResponse(responseCode = "400", description = "неправильный ввод данных для обновления токена", content = {
                       @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDto.class))})})
    public JwtAuthDto refreshToken(@Valid @RequestBody JwtAuthDto jwt) {
        return new JwtAuthDto(registrationService.refreshToken(jwt));
    }

}
