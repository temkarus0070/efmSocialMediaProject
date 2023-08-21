package org.temkarus0070.efmsocialmedia.subscribeActivity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.temkarus0070.efmsocialmedia.dto.PostDto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class SubscribeActivityTestUtils {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    public void addTwoPersonsAsFriends(String firstPersonName, String secondPersonName) throws Exception {
        mockMvc.perform(post(String.format("/user/friend/%s/add-friend", secondPersonName)).with(user(firstPersonName)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk());
        mockMvc.perform(post(String.format("/user/friend/%s/accept-friend", firstPersonName)).with(user(secondPersonName)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk());

    }

    public List<PostDto> generatePostsFromPerson(String personName, int postsCount) throws Exception {
        List<PostDto> posts = new ArrayList<>();
        for (int i = 0; i < postsCount; i++) {
            PostDto postDto = new PostDto();
            postDto.setText("kek" + i);
            postDto.setHeader("kek" + i);
            String contentAsString = mockMvc.perform(post("/post").with(user(personName))
                                                                  .contentType(MediaType.APPLICATION_JSON)
                                                                  .content(objectMapper.writeValueAsString(postDto)))
                                            .andExpect(MockMvcResultMatchers.status()
                                                                            .isCreated())
                                            .andReturn()
                                            .getResponse()
                                            .getContentAsString();

            long postId = Long.parseLong(contentAsString);

            contentAsString = mockMvc.perform(get(String.format("/post/%d", postId)).with(user(personName)))
                                     .andExpect(MockMvcResultMatchers.status()
                                                                     .isOk())
                                     .andReturn()
                                     .getResponse()
                                     .getContentAsString();

            PostDto createdPost = objectMapper.readValue(contentAsString, PostDto.class);
            posts.add(createdPost);
        }
        return posts;
    }

    public void checkFriendActivityIsShowed(String checkPersonName, String friendName, List<PostDto> friendPosts)
        throws Exception {
        Assertions.assertTrue(friendPosts.stream()
                                         .allMatch(e -> e.getAuthorName()
                                                         .equals(friendName)));

        List<PostDto> postDtoSortedList = friendPosts.stream()
                                                     .sorted(Comparator.comparing(PostDto::getCreated)
                                                                       .reversed())
                                                     .toList();
        int pageSize = 10;
        for (int i = 0; i < Math.floor((double) friendPosts.size() / pageSize); i++) {
            String contentAsString = mockMvc.perform(get("/subscribe-activity").with(user(checkPersonName))
                                                                               .contentType(MediaType.APPLICATION_JSON)
                                                                               .param("page", String.valueOf(i))
                                                                               .param("size", String.valueOf(pageSize))
                                                                               .param("sort", "created,desc"))

                                            .andExpect(MockMvcResultMatchers.status()
                                                                            .isOk())
                                            .andReturn()
                                            .getResponse()
                                            .getContentAsString();
            CustomPage customPage = objectMapper.readValue(contentAsString, CustomPage.class);
            List<PostDto> content = customPage.getContent();
            List<PostDto> expectedPageContent = postDtoSortedList.stream()
                                                                 .limit(pageSize)
                                                                 .toList();
            for (int i1 = 0; i1 < content.size(); i1++) {
                PostDto actualPost = content.get(i1);
                PostDto expectedPost = expectedPageContent.get(i1);
                Assertions.assertEquals(expectedPost, actualPost);
            }
            postDtoSortedList = postDtoSortedList.stream()
                                                 .skip(pageSize)
                                                 .collect(Collectors.toList());

        }

    }
}

@JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable"})
class CustomPage extends PageImpl<PostDto> {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CustomPage(@JsonProperty("content") List<PostDto> content,
                      @JsonProperty("number") int page,
                      @JsonProperty("size") int size,
                      @JsonProperty("totalelements") long total) {
        super(content, PageRequest.of(page, size), total);
    }

    public CustomPage(Page<PostDto> page) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
    }
}
