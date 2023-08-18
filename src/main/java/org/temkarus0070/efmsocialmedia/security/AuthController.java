package org.temkarus0070.efmsocialmedia.security;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.temkarus0070.efmsocialmedia.security.persistence.dto.JwtAuthDto;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.AuthToken;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.User;

@RestController
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping("/registrate")
    public JwtAuthDto registrate(@RequestBody User user) {
        return new JwtAuthDto(registrationService.registrateUser(user));
    }

}
