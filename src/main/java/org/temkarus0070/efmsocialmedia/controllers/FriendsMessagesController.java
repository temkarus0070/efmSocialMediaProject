package org.temkarus0070.efmsocialmedia.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.temkarus0070.efmsocialmedia.dto.DialogDto;
import org.temkarus0070.efmsocialmedia.dto.MessageDto;
import org.temkarus0070.efmsocialmedia.services.FriendsMessagesService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user/message")
public class FriendsMessagesController {

    private FriendsMessagesService friendsMessagesService;

    @GetMapping("/active-dialogs")
    public List<DialogDto> getActiveDialogs() {
        return friendsMessagesService.getActiveDialogs();
    }

    @GetMapping
    public List<MessageDto> getMessagesWithFriend(@RequestParam String friendUsername) {
        return friendsMessagesService.getMessagesWithFriend(friendUsername);
    }

    @PostMapping
    public void sendMessageToFriend(@RequestBody MessageDto messageDto) {
        friendsMessagesService.sendMessage(messageDto);
    }
}
