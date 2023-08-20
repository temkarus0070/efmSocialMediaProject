package org.temkarus0070.efmsocialmedia.dto;

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
    private String messageReceiverUsername;
    private LocalDateTime sendTime;
    private String text;
}
