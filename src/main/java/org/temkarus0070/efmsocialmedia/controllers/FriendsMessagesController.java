package org.temkarus0070.efmsocialmedia.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.temkarus0070.efmsocialmedia.dto.DialogDto;
import org.temkarus0070.efmsocialmedia.dto.MessageDto;
import org.temkarus0070.efmsocialmedia.security.dto.ErrorDto;
import org.temkarus0070.efmsocialmedia.services.FriendsMessagesService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user/message")
@Tag(name = "Friends messages API", description = "Переписка с друзьями")
public class FriendsMessagesController {

    private FriendsMessagesService friendsMessagesService;

    @GetMapping("/active-dialogs")
    @Operation(description = "получить активные чаты с друзьями")
    public List<DialogDto> getActiveDialogs() {
        return friendsMessagesService.getActiveDialogs();
    }

    @GetMapping
    @Operation(description = "Получить все сообщения с другом")
    public List<MessageDto> getMessagesWithFriend(
        @RequestParam @NotBlank(message = "имя пользователя должно быть не пустым") String friendUsername) {
        return friendsMessagesService.getMessagesWithFriend(friendUsername);
    }

    @PostMapping
    @Operation(description = "отправить сообщение другу", responses = {
        @ApiResponse(responseCode = "404", description = "друг не найден в списке друзей",
                     content = @Content(schema = @Schema(implementation = ErrorDto.class))),
        @ApiResponse(responseCode = "200", description = "сообщение успешно отправлено")})
    public void sendMessageToFriend(@Valid @RequestBody MessageDto messageDto) {
        friendsMessagesService.sendMessage(messageDto);
    }
}
