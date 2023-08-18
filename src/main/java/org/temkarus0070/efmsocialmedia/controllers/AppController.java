package org.temkarus0070.efmsocialmedia.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @GetMapping("/app")
    public void app() {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        System.out.println(authentication);
    }
}
