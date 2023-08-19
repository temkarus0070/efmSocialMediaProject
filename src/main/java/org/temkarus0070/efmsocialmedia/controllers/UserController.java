package org.temkarus0070.efmsocialmedia.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.temkarus0070.efmsocialmedia.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/user/friend")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping
    public List<String> getFriends() {
        return userService.getFriendList();
    }

    @GetMapping("/subscriber")
    public List<String> getSubscribers() {
        return userService.getSubscribersList();
    }

    @GetMapping("/subscribe")
    public List<String> getSubscribes() {
        return userService.getSubscribeList();
    }

    @PostMapping("/{friendUsername}/add-friend")
    public void sendFriendRequest(@PathVariable String friendUsername) {
        userService.sendFriendRequest(friendUsername);
    }

    @PostMapping("/{friendUsername}/accept-friend")
    public void acceptFriendRequest(@PathVariable String friendUsername) {
        userService.acceptFriendRequest(friendUsername);
    }


    @PostMapping("/{friendUsername}/decline-friend-request")
    public void declineFriendRequest(@PathVariable String friendUsername) {
        userService.declineFriendRequest(friendUsername);
    }


    @PostMapping("/{friendUsername}/remove-my-friend-request")
    public void removeMyFriendRequest(@PathVariable String friendUsername) {
        userService.removeFriendRequest(friendUsername);
    }


}
