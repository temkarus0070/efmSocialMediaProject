package org.temkarus0070.efmsocialmedia.friends;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class NegativeCasesTest extends BaseFriendTest {

    @Test
    public void testFriendListWithoutAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/friend")
                                              .with(SecurityMockMvcRequestPostProcessors.anonymous()))
               .andExpect(MockMvcResultMatchers.status()
                                               .isUnauthorized());
    }

    @Test
    public void testAddIncorrectFriend() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/friend/pup777/add-friend")
                                              .with(SecurityMockMvcRequestPostProcessors.user("temkarus0070")))
               .andExpect(MockMvcResultMatchers.status()
                                               .isNotFound());
    }

    @Test
    public void testAcceptIncorrectFriend() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/friend/pup777/accept-friend")
                                              .with(SecurityMockMvcRequestPostProcessors.user("temkarus0070")))
               .andExpect(MockMvcResultMatchers.status()
                                               .isNotFound());
    }

    @Test
    public void testAcceptPersonWithoutFriendRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/user/friend/pupkin777/accept-friend")
                                              .with(SecurityMockMvcRequestPostProcessors.user("temkarus0070")))
               .andExpect(MockMvcResultMatchers.status()
                                               .isNotFound());
    }

}
