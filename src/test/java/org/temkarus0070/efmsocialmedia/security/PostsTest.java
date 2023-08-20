package org.temkarus0070.efmsocialmedia.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
public class PostsTest {

    @Test
    @WithMockUser(username = "temkarus0070")
    public void test() {
        Authentication authentication = SecurityContextHolder.getContext()
                                                             .getAuthentication();
        System.out.println(authentication);
    }
}
