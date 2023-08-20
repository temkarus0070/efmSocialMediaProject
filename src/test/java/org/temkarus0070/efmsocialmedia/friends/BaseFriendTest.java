package org.temkarus0070.efmsocialmedia.friends;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.temkarus0070.efmsocialmedia.BaseApiTest;
import org.temkarus0070.efmsocialmedia.repositories.RelationshipRepository;
import org.temkarus0070.efmsocialmedia.security.persistence.entities.UserAccount;
import org.temkarus0070.efmsocialmedia.security.services.RegistrationService;

public class BaseFriendTest extends BaseApiTest {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private RelationshipRepository relationshipRepository;

    @BeforeAll
    public void init() {
        registrationService.registrateUser(new UserAccount("temkarus0070", "1@a.ru", "1234", true));
        registrationService.registrateUser(new UserAccount("pupkin777", "11@a.ru", "1234", true));

    }

    @AfterEach
    public void clean() {
        relationshipRepository.deleteAll();
    }

}
