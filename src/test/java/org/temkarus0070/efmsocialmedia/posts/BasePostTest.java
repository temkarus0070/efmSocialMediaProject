package org.temkarus0070.efmsocialmedia.posts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.temkarus0070.efmsocialmedia.BaseApiTest;
import org.temkarus0070.efmsocialmedia.repositories.PostRepository;

public class BasePostTest extends BaseApiTest {


    protected ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PostRepository postRepository;


    @BeforeEach
    public void initialize() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterEach
    public void clean() {
        postRepository.deleteAll();
    }

}
