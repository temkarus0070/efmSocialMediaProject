package org.temkarus0070.efmsocialmedia.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/incoming-requests")
    public List<String> getIncomingFriendRequests() {
        return userService.getFriendsRequestsList();
    }

    @PostMapping("/{friendUsername}/add-friend")
    public void sendFriendRequest(@PathVariable String friendUsername) {
        userService.sendFriendRequest(friendUsername);
    }

    @PostMapping("/{friendUsername}/accept-friend")
    public void acceptFriendRequest(@PathVariable String friendUsername) {
        userService.acceptFriendRequest(friendUsername);
    }




    @DeleteMapping("/{friendUsername}")
    public void removeMyFriendRequest(@PathVariable String friendUsername) {
        userService.removeFriend(friendUsername);
    }


}
