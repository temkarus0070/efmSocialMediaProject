package org.temkarus0070.efmsocialmedia.friends;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.temkarus0070.efmsocialmedia.BaseApiTest;
import org.temkarus0070.efmsocialmedia.repositories.RelationshipRepository;

public class BaseFriendTest extends BaseApiTest {
    @Autowired
    private RelationshipRepository relationshipRepository;


    @AfterEach
    public void clean() {
        relationshipRepository.deleteAll();
    }

}
