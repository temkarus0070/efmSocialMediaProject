package org.temkarus0070.efmsocialmedia.posts;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.assertj.core.matcher.AssertionMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.temkarus0070.efmsocialmedia.dto.ImageDto;
import org.temkarus0070.efmsocialmedia.dto.PostDto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Component
public class PostUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public void checkImageSuccessfulRemoved(long imageId, long postId) throws Exception {
        String contentAsString = mockMvc.perform(get(String.format("/post/%d", postId)))
                                        .andExpect(MockMvcResultMatchers.status()
                                                                        .isOk())
                                        .andReturn()
                                        .getResponse()
                                        .getContentAsString();
        PostDto postDto = objectMapper.readValue(contentAsString, PostDto.class);

        mockMvc.perform(delete(String.format("/post/%d/image/%d", postId, imageId)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk());

        PostDto finalPostDto = postDto;

        assert finalPostDto.getImages()
                           .stream()
                           .anyMatch(e -> e.getId() == imageId);

        mockMvc.perform(get(String.format("/post/%d", postId)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk())
               .andExpect(MockMvcResultMatchers.content()
                                               .string(new AssertionMatcher<>() {
                                                   @SneakyThrows
                                                   @Override
                                                   public void assertion(String actual) throws AssertionError {
                                                       PostDto updatedPost = objectMapper.readValue(actual, PostDto.class);
                                                       assert updatedPost.getId() == postId;
                                                       assert updatedPost.getCreated()
                                                                         .equals(finalPostDto.getCreated());
                                                       assert updatedPost.getText()
                                                                         .equals(finalPostDto.getText());
                                                       assert updatedPost.getHeader()
                                                                         .equals(finalPostDto.getHeader());
                                                       assert updatedPost.getAuthorName()
                                                                         .equals(finalPostDto.getAuthorName());
                                                       assert updatedPost.getImages()
                                                                         .stream()
                                                                         .anyMatch(e -> Arrays.equals(e.getContent(),
                                                                                                      finalPostDto.getImages()
                                                                                                                  .get(0)
                                                                                                                  .getContent()));
                                                       assert updatedPost.getImages()
                                                                         .stream()
                                                                         .noneMatch(e -> e.getId() == imageId);
                                                   }
                                               }));

    }

    public long checkPostSuccessCreatedAndAddedToApp(PostDto postDto) throws Exception {

        LocalDateTime now = LocalDateTime.now();
        postDto.setHeader("new");
        postDto.setText("new update");
        String content = objectMapper.writeValueAsString(postDto);
        String postIdStr = mockMvc.perform(post("/post").contentType(MediaType.APPLICATION_JSON)
                                                        .content(content))
                                  .andExpect(MockMvcResultMatchers.status()
                                                                  .isCreated())
                                  .andExpect(MockMvcResultMatchers.content()
                                                                  .string(new AssertionMatcher<String>() {
                                                                      @SneakyThrows
                                                                      @Override
                                                                      public void assertion(String actual) throws AssertionError {
                                                                          long id = objectMapper.readValue(actual, Long.class);
                                                                      }
                                                                  }))
                                  .andReturn()
                                  .getResponse()
                                  .getContentAsString();

        long postId = Long.parseLong(postIdStr);
        mockMvc.perform(get(String.format("/post/%d", postId)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk())
               .andExpect(MockMvcResultMatchers.content()
                                               .string(new AssertionMatcher<>() {
                                                   @SneakyThrows
                                                   @Override
                                                   public void assertion(String actual) throws AssertionError {
                                                       PostDto postDto1 = objectMapper.readValue(actual, PostDto.class);
                                                       assert postDto1.getId() == postId;
                                                       assert postDto1.getCreated()
                                                                      .isAfter(now);
                                                       assert postDto1.getText()
                                                                      .equals(postDto.getText());
                                                       assert postDto1.getHeader()
                                                                      .equals(postDto.getHeader());
                                                       assert postDto1.getAuthorName()
                                                                      .equals("temkarus0070");
                                                       assert postDto1.getImages()
                                                                      .stream()
                                                                      .allMatch(e -> Arrays.equals(e.getContent(),
                                                                                                   postDto1.getImages()
                                                                                                           .get(0)
                                                                                                           .getContent()));
                                                   }
                                               }));
        return postId;

    }

    public long checkImageSuccessfulAdded(PostDto postDto) throws Exception {
        long postId = postDto.getId();
        String contentAsString = mockMvc.perform(get(String.format("/post/%d", postId)))
                                        .andExpect(MockMvcResultMatchers.status()
                                                                        .isOk())
                                        .andReturn()
                                        .getResponse()
                                        .getContentAsString();
        postDto = objectMapper.readValue(contentAsString, PostDto.class);

        List<ImageDto> newImages = createSomePost().getImages();
        String contentAsStringWhenAddImage = mockMvc.perform(post(String.format("/post/%d/image",
                                                                                postId)).content(objectMapper.writeValueAsString(
                                                        newImages.get(0))))
                                                    .andExpect(MockMvcResultMatchers.status()
                                                                                    .isCreated())
                                                    .andReturn()
                                                    .getResponse()
                                                    .getContentAsString();

        long imageId = Long.parseLong(contentAsStringWhenAddImage);
        PostDto finalPostDto = postDto;
        mockMvc.perform(get(String.format("/post/%d", postId)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk())
               .andExpect(MockMvcResultMatchers.content()
                                               .string(new AssertionMatcher<>() {
                                                   @SneakyThrows
                                                   @Override
                                                   public void assertion(String actual) throws AssertionError {
                                                       PostDto updatedPost = objectMapper.readValue(actual, PostDto.class);
                                                       assert updatedPost.getId() == postId;
                                                       assert updatedPost.getCreated()
                                                                         .equals(finalPostDto.getCreated());
                                                       assert updatedPost.getText()
                                                                         .equals(finalPostDto.getText());
                                                       assert updatedPost.getHeader()
                                                                         .equals(finalPostDto.getHeader());
                                                       assert updatedPost.getAuthorName()
                                                                         .equals(finalPostDto.getAuthorName());
                                                       assert updatedPost.getImages()
                                                                         .stream()
                                                                         .anyMatch(e -> Arrays.equals(e.getContent(),
                                                                                                      finalPostDto.getImages()
                                                                                                                  .get(0)
                                                                                                                  .getContent()));
                                                       assert updatedPost.getImages()
                                                                         .stream()
                                                                         .anyMatch(e -> Arrays.equals(e.getContent(),
                                                                                                      newImages.get(0)
                                                                                                               .getContent()));
                                                   }
                                               }));
        return imageId;
    }

    public void checkPostNotSuccessEditedByNotAuthor(PostDto postDto, String otherPerson) throws Exception {

        long postId = postDto.getId();
        String contentAsString = mockMvc.perform(get(String.format("/post/%d", postId)))
                                        .andExpect(MockMvcResultMatchers.status()
                                                                        .isOk())
                                        .andReturn()
                                        .getResponse()
                                        .getContentAsString();
        PostDto original = objectMapper.readValue(contentAsString, PostDto.class);
        postDto = objectMapper.readValue(contentAsString, PostDto.class);
        postDto.setText("new text");
        postDto.setHeader("new");
        String content = objectMapper.writeValueAsString(postDto);
        mockMvc.perform(patch("/post").contentType(MediaType.APPLICATION_JSON)
                                      .content(content)
                                      .with(user(otherPerson)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isForbidden());

        mockMvc.perform(get(String.format("/post/%d", postId)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk())
               .andExpect(MockMvcResultMatchers.content()
                                               .string(new AssertionMatcher<>() {
                                                   @SneakyThrows
                                                   @Override
                                                   public void assertion(String actual) throws AssertionError {
                                                       PostDto updatedPost = objectMapper.readValue(actual, PostDto.class);
                                                       assert updatedPost.getId() == postId;
                                                       assert updatedPost.getCreated()
                                                                         .equals(original.getCreated());
                                                       assert updatedPost.getText()
                                                                         .equals(original.getText());
                                                       assert updatedPost.getHeader()
                                                                         .equals(original.getHeader());
                                                       assert updatedPost.getAuthorName()
                                                                         .equals(original.getAuthorName());
                                                       assert updatedPost.getImages()
                                                                         .stream()
                                                                         .allMatch(e -> Arrays.equals(e.getContent(),
                                                                                                      original.getImages()
                                                                                                              .get(0)
                                                                                                              .getContent()));
                                                   }
                                               }));

    }

    public void checkPostSuccessEdited(PostDto postDto) throws Exception {

        long postId = postDto.getId();
        String contentAsString = mockMvc.perform(get(String.format("/post/%d", postId)))
                                        .andExpect(MockMvcResultMatchers.status()
                                                                        .isOk())
                                        .andReturn()
                                        .getResponse()
                                        .getContentAsString();

        postDto = objectMapper.readValue(contentAsString, PostDto.class);
        postDto.setText("new text");
        postDto.setHeader("new");
        String content = objectMapper.writeValueAsString(postDto);
        mockMvc.perform(patch("/post").contentType(MediaType.APPLICATION_JSON)
                                      .content(content))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk());

        PostDto finalPostDto = postDto;
        mockMvc.perform(get(String.format("/post/%d", postId)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk())
               .andExpect(MockMvcResultMatchers.content()
                                               .string(new AssertionMatcher<>() {
                                                   @SneakyThrows
                                                   @Override
                                                   public void assertion(String actual) throws AssertionError {
                                                       PostDto updatedPost = objectMapper.readValue(actual, PostDto.class);
                                                       assert updatedPost.getId() == postId;
                                                       assert updatedPost.getCreated()
                                                                         .equals(finalPostDto.getCreated());
                                                       assert updatedPost.getText()
                                                                         .equals(finalPostDto.getText());
                                                       assert updatedPost.getHeader()
                                                                         .equals(finalPostDto.getHeader());
                                                       assert updatedPost.getAuthorName()
                                                                         .equals(finalPostDto.getAuthorName());
                                                       assert updatedPost.getImages()
                                                                         .stream()
                                                                         .allMatch(e -> Arrays.equals(e.getContent(),
                                                                                                      finalPostDto.getImages()
                                                                                                                  .get(0)
                                                                                                                  .getContent()));
                                                   }
                                               }));

    }

    public PostDto createSomePost() {
        Random random = new Random(LocalDateTime.now()
                                                .toEpochSecond(ZoneOffset.UTC));
        byte[] image = new byte[300];
        random.nextBytes(image);
        PostDto postDto = new PostDto();
        postDto.setHeader("first post on site");
        postDto.setText("it is first post on site");
        postDto.getImages()
               .add(new ImageDto(0, image));
        return postDto;
    }

    public void checkImageCantBeAddedByNotAuthor(PostDto postDto, String notAuthorName) throws Exception {
        long postId = postDto.getId();
        String contentAsString = mockMvc.perform(get(String.format("/post/%d", postId)))
                                        .andExpect(MockMvcResultMatchers.status()
                                                                        .isOk())
                                        .andReturn()
                                        .getResponse()
                                        .getContentAsString();
        postDto = objectMapper.readValue(contentAsString, PostDto.class);

        List<ImageDto> newImages = createSomePost().getImages();
        mockMvc.perform(post(String.format("/post/%d/image", postId)).content(objectMapper.writeValueAsString(newImages.get(0)))
                                                                     .with(user(notAuthorName)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isForbidden());

        PostDto finalPostDto = postDto;
        mockMvc.perform(get(String.format("/post/%d", postId)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk())
               .andExpect(MockMvcResultMatchers.content()
                                               .string(new AssertionMatcher<>() {
                                                   @SneakyThrows
                                                   @Override
                                                   public void assertion(String actual) throws AssertionError {
                                                       PostDto updatedPost = objectMapper.readValue(actual, PostDto.class);
                                                       assert updatedPost.getId() == postId;
                                                       assert updatedPost.getCreated()
                                                                         .equals(finalPostDto.getCreated());
                                                       assert updatedPost.getText()
                                                                         .equals(finalPostDto.getText());
                                                       assert updatedPost.getHeader()
                                                                         .equals(finalPostDto.getHeader());
                                                       assert updatedPost.getAuthorName()
                                                                         .equals(finalPostDto.getAuthorName());
                                                       assert updatedPost.getImages()
                                                                         .stream()
                                                                         .anyMatch(e -> Arrays.equals(e.getContent(),
                                                                                                      finalPostDto.getImages()
                                                                                                                  .get(0)
                                                                                                                  .getContent()));
                                                       assert updatedPost.getImages()
                                                                         .size() == finalPostDto.getImages()
                                                                                                .size();
                                                   }
                                               }));
    }

    public void checkImageCantBeRemovedByNotAuthor(PostDto postDto, String notAuthorName) throws Exception {
        long postId = postDto.getId();
        String contentAsString = mockMvc.perform(get(String.format("/post/%d", postId)))
                                        .andExpect(MockMvcResultMatchers.status()
                                                                        .isOk())
                                        .andReturn()
                                        .getResponse()
                                        .getContentAsString();
        postDto = objectMapper.readValue(contentAsString, PostDto.class);

        List<ImageDto> newImages = createSomePost().getImages();
        mockMvc.perform(delete(String.format("/post/%d/image/%d",
                                             postId,
                                             postDto.getImages()
                                                    .stream()
                                                    .findAny()
                                                    .get()
                                                    .getId())).with(user(notAuthorName)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isForbidden());

        PostDto finalPostDto = postDto;
        mockMvc.perform(get(String.format("/post/%d", postId)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk())
               .andExpect(MockMvcResultMatchers.content()
                                               .string(new AssertionMatcher<>() {
                                                   @SneakyThrows
                                                   @Override
                                                   public void assertion(String actual) throws AssertionError {
                                                       PostDto updatedPost = objectMapper.readValue(actual, PostDto.class);
                                                       assert updatedPost.getId() == postId;
                                                       assert updatedPost.getCreated()
                                                                         .equals(finalPostDto.getCreated());
                                                       assert updatedPost.getText()
                                                                         .equals(finalPostDto.getText());
                                                       assert updatedPost.getHeader()
                                                                         .equals(finalPostDto.getHeader());
                                                       assert updatedPost.getAuthorName()
                                                                         .equals(finalPostDto.getAuthorName());
                                                       assert updatedPost.getImages()
                                                                         .stream()
                                                                         .anyMatch(e -> Arrays.equals(e.getContent(),
                                                                                                      finalPostDto.getImages()
                                                                                                                  .get(0)
                                                                                                                  .getContent()));
                                                       assert updatedPost.getImages()
                                                                         .size() == finalPostDto.getImages()
                                                                                                .size();
                                                   }
                                               }));
    }

    public void checkPostSuccessRemoved(long postId) throws Exception {
        mockMvc.perform(delete(String.format("/post/%d", postId)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isOk());

        mockMvc.perform(get(String.format("/post/%d", postId)))
               .andExpect(MockMvcResultMatchers.status()
                                               .isNotFound());
    }
}
