package org.temkarus0070.efmsocialmedia;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.UserAccount;
import org.temkarus0070.efmsocialmedia.security.services.RegistrationService;

@ActiveProfiles("test")
@SpringBootTest()
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class BaseApiTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private RegistrationService registrationService;

    @BeforeAll
    public void init() {
        registrationService.registrateUser(new UserAccount("temkarus0070", "1@a.ru", "1234", true));
        registrationService.registrateUser(new UserAccount("pupkin777", "11@a.ru", "1234", true));

    }
}
