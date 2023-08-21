package org.temkarus0070.efmsocialmedia.utils;

import org.temkarus0070.efmsocialmedia.dto.ImageDto;
import org.temkarus0070.efmsocialmedia.dto.PostDto;
import org.temkarus0070.efmsocialmedia.entities.Image;
import org.temkarus0070.efmsocialmedia.entities.Post;
import org.temkarus0070.efmsocialmedia.entities.User;

import java.util.stream.Collectors;

public class PostUtils {

    public static PostDto mapFromPost(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setCreated(post.getCreated());
        postDto.setAuthorName(post.getAuthor()
                                  .getUsername());
        postDto.setHeader(post.getHeader());
        postDto.setText(post.getText());
        postDto.setImages(post.getImages()
                              .stream()
                              .map(e -> {
                                  ImageDto imageDto = new ImageDto();
                                  imageDto.setContent(e.getContent());
                                  imageDto.setId(e.getId());
                                  return imageDto;
                              })
                              .collect(Collectors.toList()));
        return postDto;
    }

    public static Post mapToPost(PostDto postDto) {
        Post post = new Post();
        post.setId(postDto.getId());
        post.setCreated(postDto.getCreated());
        post.setAuthor(new User(postDto.getAuthorName()));
        post.setHeader(postDto.getHeader());
        post.setText(postDto.getText());
        post.setImages(postDto.getImages()
                              .stream()
                              .map(e -> {
                                  Image imageDto = new Image();
                                  imageDto.setContent(e.getContent());
                                  imageDto.setId(e.getId());
                                  imageDto.setPost(post);
                                  return imageDto;
                              })
                              .collect(Collectors.toList()));
        return post;
    }

}
