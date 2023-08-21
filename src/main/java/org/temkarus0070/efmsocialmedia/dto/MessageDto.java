package org.temkarus0070.efmsocialmedia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    private String messageSender;
    @NotNull(message = "Получатель сообщения не должен быть равным null")
    @NotBlank(message = "Получатель сообщения не может быть пустым полем")
    private String messageReceiverUsername;
    private LocalDateTime sendTime;

    @NotNull(message = "Текст сообщения не должен быть равным null")
    @NotBlank(message = "Текст сообщения не может быть пустым полем")
    private String text;
}
