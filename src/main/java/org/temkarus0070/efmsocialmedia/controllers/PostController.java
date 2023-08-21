package org.temkarus0070.efmsocialmedia.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.temkarus0070.efmsocialmedia.dto.PostDto;
import org.temkarus0070.efmsocialmedia.services.PostService;

@RestController
@AllArgsConstructor
@RequestMapping("/post")
public class PostController {

    private PostService postService;

    @GetMapping("/{postId}")
    public PostDto get(@PathVariable long postId) {
        return postService.get(postId);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public long create(@RequestBody PostDto post) {
        return postService.create(post)
                          .getId();
    }

    @PatchMapping
    public void edit(@RequestBody PostDto post) {
        postService.edit(post);
    }

    @PostMapping("/{postId}/image")
    public long addImage(@PathVariable long postId, @RequestBody byte[] image) {
        return postService.addImage(postId, image)
                          .getImages()
                          .stream()
                          .findAny()
                          .get()
                          .getId();
    }

    @DeleteMapping("/{postId}/image/{imageId}")
    public void removeImage(@PathVariable long postId, @PathVariable long imageId) {
        postService.removeImage(postId, imageId);
    }


    @DeleteMapping("/{postId}")
    public void remove(@PathVariable long postId) {
        postService.remove(postId);
    }

}
