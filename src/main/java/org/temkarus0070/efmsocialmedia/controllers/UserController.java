package org.temkarus0070.efmsocialmedia.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.temkarus0070.efmsocialmedia.security.dto.ErrorDto;
import org.temkarus0070.efmsocialmedia.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/user/friend")
@AllArgsConstructor
@Tag(name = "Friends API", description = "Работа с друзьями")
public class UserController {

    private UserService userService;

    @GetMapping
    @Operation(description = "получить список имён своих друзей")
    public List<String> getFriends() {
        return userService.getFriendList();
    }

    @GetMapping("/subscriber")
    @Operation(description = "получить список имён своих подписчиков")
    public List<String> getSubscribers() {
        return userService.getSubscribersList();
    }

    @GetMapping("/subscribe")
    @Operation(description = "получить список имён тех на кого мы подписаны")
    public List<String> getSubscribes() {
        return userService.getSubscribeList();
    }

    @GetMapping("/incoming-requests")
    @Operation(description = "получить список имён пользователей, от которых есть заявки на добавление в друзья")
    public List<String> getIncomingFriendRequests() {
        return userService.getFriendsRequestsList();
    }

    @PostMapping("/{friendUsername}/add-friend")
    @Operation(description = "отправить запрос на дружбу",
               responses = {@ApiResponse(description = "запрос отправлен", responseCode = "200"), @ApiResponse(
                   description = "некорректный запрос ( заявка была отправлена ранее или уже друзья или пользователь отправил нас в подписчики или не найден пользователь с таким именем)",
                   responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    public void sendFriendRequest(@PathVariable String friendUsername) {
        userService.sendFriendRequest(friendUsername);
    }

    @PostMapping("/{friendUsername}/accept-friend")
    @Operation(description = "принять запрос на дружбу",
               responses = {@ApiResponse(description = "запрос принят", responseCode = "200"),
                   @ApiResponse(description = "некорректный запрос ( уже друзья или не найден запрос на дружбу)",
                                responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    public void acceptFriendRequest(@PathVariable String friendUsername) {
        userService.acceptFriendRequest(friendUsername);
    }


    @DeleteMapping("/{friendUsername}")
    @Operation(description = "удалить из друзей/отписаться/убрать заявку на добавление в друзья",
               responses = {@ApiResponse(description = "друг удален", responseCode = "200"),
                   @ApiResponse(description = "некорректный запрос (не найден запрос на дружбу)", responseCode = "400",
                                content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    public void removeMyFriendRequest(@PathVariable String friendUsername) {
        userService.removeFriend(friendUsername);
    }

}
