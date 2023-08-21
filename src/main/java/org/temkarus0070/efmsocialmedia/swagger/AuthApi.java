package org.temkarus0070.efmsocialmedia.swagger;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.temkarus0070.efmsocialmedia.security.dto.ErrorDto;
import org.temkarus0070.efmsocialmedia.security.dto.JwtAuthDto;


@RestController
@Tag(name = "API для аутентификации/регистрации", description = "Аутентификация, регистрация и все связанное с этим")
public class AuthApi {

    @Operation(description = "вход в приложение под своим логином и паролем и получение токенов авторизации")
    @ApiResponses({@ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                                         schema = @Schema(implementation = JwtAuthDto.class)),
                                description = "успешная авторизация и получение токена"), @ApiResponse(responseCode = "401",
                                                                                                       content = @Content(
                                                                                                           mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                                                                           schema = @Schema(
                                                                                                               implementation = ErrorDto.class)),
                                                                                                       description = "неуспешная авторизация")})
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void login(FormUser userDetails) {

    }

    ;

}

@Getter
class FormUser {

    private String username;
    private String password;
}