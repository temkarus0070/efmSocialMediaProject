package org.temkarus0070.efmsocialmedia.controllers;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.temkarus0070.efmsocialmedia.entities.Post;
import org.temkarus0070.efmsocialmedia.services.SubscribeActivityService;

@RestController
@AllArgsConstructor
@RequestMapping("/subscribe-activity")
public class SubscribeActivityController {
    private SubscribeActivityService subscribeActivityService;

    @GetMapping
    public Page<Post> get(@RequestBody PageRequest pageable) {
        return subscribeActivityService.getLastPosts(pageable);
    }
}
