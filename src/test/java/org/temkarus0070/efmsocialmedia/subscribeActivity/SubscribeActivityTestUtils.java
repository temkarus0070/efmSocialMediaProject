package org.temkarus0070.efmsocialmedia.subscribeActivity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.temkarus0070.efmsocialmedia.dto.PostDto;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class SubscribeActivityTestUtils {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    public void addTwoPersonsAsFriends(String firstPersonName, String secondPersonName) throws Exception {
        mockMvc.perform(post(String.format("/user/friend/{%s}/add-friend", secondPersonName)).with(user(firstPersonName)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk());
        mockMvc.perform(post(String.format("/user/friend/{%s}/accept-friend", firstPersonName)).with(user(secondPersonName)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk());

    }

    public List<PostDto> generatePostsFromPerson(String personName, int postsCount) {
        for (int i = 0; i < postsCount; i++) {
            mockMvc.perform(post("/post"))
        }
    }
}
