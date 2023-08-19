package org.temkarus0070.efmsocialmedia.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.temkarus0070.efmsocialmedia.entities.Post;
import org.temkarus0070.efmsocialmedia.services.PostService;

@RestController
@AllArgsConstructor
@RequestMapping("/post")
public class PostController {
    private PostService postService;

    @GetMapping("/{postId}")
    public Post get(@PathVariable long postId) {
        return postService.get(postId);
    }

    @PostMapping
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PatchMapping
    public void edit(@RequestBody Post post) {
        postService.edit(post);
    }

    @PostMapping("/{postId}/image")
    public void addImage(@PathVariable long postId, @RequestBody byte[] image) {
        postService.addImage(postId, image);
    }


    @DeleteMapping("/{postId}")
    public void remove(@PathVariable long postId) {
        postService.remove(postId);
    }


}
