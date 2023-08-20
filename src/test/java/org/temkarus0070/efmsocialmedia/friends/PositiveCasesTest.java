package org.temkarus0070.efmsocialmedia.friends;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.assertj.core.matcher.AssertionMatcher;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

public class PositiveCasesTest extends BaseFriendTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testFriendListWhenSuccessAuth() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/friend")
                                              .with(user("temkarus0070")))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk());
    }


    @Test
    public void testAddFriendlyUser() throws Exception {
        checkSuccessfulFriendshipRequestWasSent("temkarus0070", "pupkin777");

        checkThatFriendshipRequesterDontHaveRequestButOtherPersonHave("temkarus0070", "pupkin777");
        checkThatDontHaveFriendsAndNotSubscribeToAnyone("pupkin777");
        checkThatFriendshipRequesterIsSubscriber("temkarus0070", "pupkin777");

        checkSuccessfulFriendshipRequestWasDeleted("temkarus0070", "pupkin777");

        checkThatPersonDontHaveAnyFriendshipRequest("pupkin777");

    }

    private void checkSuccessfulFriendshipRequestWasDeleted(String friendshipRequesterUsername, String friendName)
        throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("/user/friend/%s", friendName))
                                              .with(SecurityMockMvcRequestPostProcessors.user(friendshipRequesterUsername)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk());
    }

    private void checkThatPersonDontHaveAnyFriendshipRequest(String friendName) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/friend/incoming-requests")
                                              .with(SecurityMockMvcRequestPostProcessors.user(friendName)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk())
               .andExpect(MockMvcResultMatchers.content()
                                               .string(new AssertionMatcher<>() {
                                                   @SneakyThrows
                                                   @Override
                                                   public void assertion(String actual) throws AssertionError {
                                                       List<String> list = objectMapper.readValue(actual, List.class);
                                                       assert list.isEmpty();
                                                   }
                                               }));
    }

    private void checkThatFriendshipRequesterIsSubscriber(String friendshipRequesterUsername, String friendName)
        throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/user/friend/subscriber")
                                              .with(SecurityMockMvcRequestPostProcessors.user(friendName)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk())
               .andExpect(MockMvcResultMatchers.content()
                                               .string(new AssertionMatcher<>() {
                                                   @SneakyThrows
                                                   @Override
                                                   public void assertion(String actual) throws AssertionError {
                                                       List<String> list = objectMapper.readValue(actual, List.class);
                                                       assert list.contains(friendshipRequesterUsername);
                                                   }
                                               }));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/friend/subscribe")
                                              .with(SecurityMockMvcRequestPostProcessors.user(friendshipRequesterUsername)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk())
               .andExpect(MockMvcResultMatchers.content()
                                               .string(new AssertionMatcher<>() {
                                                   @SneakyThrows
                                                   @Override
                                                   public void assertion(String actual) throws AssertionError {
                                                       List<String> list = objectMapper.readValue(actual, List.class);
                                                       assert list.contains(friendName);
                                                   }
                                               }));
    }

    private void checkThatDontHaveFriendsAndNotSubscribeToAnyone(String username) throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/user/friend")
                                              .with(SecurityMockMvcRequestPostProcessors.user(username)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk())
               .andExpect(MockMvcResultMatchers.content()
                                               .string(new AssertionMatcher<>() {
                                                   @SneakyThrows
                                                   @Override
                                                   public void assertion(String actual) throws AssertionError {
                                                       List<String> list = objectMapper.readValue(actual, List.class);
                                                       assert list.isEmpty();
                                                   }
                                               }));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/friend/subscribe")
                                              .with(SecurityMockMvcRequestPostProcessors.user(username)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk())
               .andExpect(MockMvcResultMatchers.content()
                                               .string(new AssertionMatcher<>() {
                                                   @SneakyThrows
                                                   @Override
                                                   public void assertion(String actual) throws AssertionError {
                                                       List<String> list = objectMapper.readValue(actual, List.class);
                                                       assert list.isEmpty();
                                                   }
                                               }));
    }

    private void checkSuccessfulFriendshipRequestWasSent(String friendshipRequesterName, String friendName) throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(String.format("/user/friend/%s/add-friend", friendName))
                                              .with(SecurityMockMvcRequestPostProcessors.user(friendshipRequesterName)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk());
    }

    private void checkThatFriendshipRequesterDontHaveRequestButOtherPersonHave(String friendshipRequesterName, String friendName)
        throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/friend/incoming-requests")
                                              .with(SecurityMockMvcRequestPostProcessors.user(friendName)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk())
               .andExpect(MockMvcResultMatchers.content()
                                               .string(new AssertionMatcher<>() {
                                                   @SneakyThrows
                                                   @Override
                                                   public void assertion(String actual) throws AssertionError {
                                                       List<String> list = objectMapper.readValue(actual, List.class);
                                                       assert list.contains(friendshipRequesterName);
                                                   }
                                               }));

        mockMvc.perform(MockMvcRequestBuilders.get("/user/friend/incoming-requests")
                                              .with(SecurityMockMvcRequestPostProcessors.user(friendshipRequesterName)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk())
               .andExpect(MockMvcResultMatchers.content()
                                               .string(new AssertionMatcher<>() {
                                                   @SneakyThrows
                                                   @Override
                                                   public void assertion(String actual) throws AssertionError {
                                                       List<String> list = objectMapper.readValue(actual, List.class);
                                                       assert list.isEmpty();
                                                   }
                                               }));
    }

    @Test
    public void testAddFriendlyUserAndRequestAccepted() throws Exception {
        checkSuccessfulFriendshipRequestWasSent("temkarus0070", "pupkin777");
        checkThatFriendshipRequesterDontHaveRequestButOtherPersonHave("temkarus0070", "pupkin777");
        checkSuccessfulFriendshipRequestWasAccept("temkarus0070", "pupkin777");

        checkThatPersonDontHaveAnyFriendshipRequest("pupkin777");

        checkSuccessfulFriendshipRequestWasDeleted("temkarus0070", "pupkin777");

        checkThatPersonDontHaveAnyFriendshipRequest("pupkin777");

        checkThatFriendshipRequesterIsSubscriber("pupkin777", "temkarus0070");

        checkThatDontHaveFriendsAndNotSubscribeToAnyone("temkarus0070");

    }

    private void checkSuccessfulFriendshipRequestWasAccept(String friendshipRequesterName, String friendName) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(String.format("/user/friend/%s/accept-friend", friendshipRequesterName))
                                              .with(SecurityMockMvcRequestPostProcessors.user(friendName)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk());
    }
}
