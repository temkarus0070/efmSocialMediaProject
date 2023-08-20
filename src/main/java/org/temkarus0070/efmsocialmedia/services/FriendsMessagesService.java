package org.temkarus0070.efmsocialmedia.services;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.temkarus0070.efmsocialmedia.dto.MessageDto;
import org.temkarus0070.efmsocialmedia.entities.Message;
import org.temkarus0070.efmsocialmedia.entities.MessageId;
import org.temkarus0070.efmsocialmedia.repositories.MessagesRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class FriendsMessagesService {

    private MessagesRepository messagesRepository;
    private UserService userService;

    public List<MessageDto> getMessages() {
        if (userService.hasFriend(messageDto.getMessageReceiverUsername())) {
            Authentication authentication = SecurityContextHolder.getContext()
                                                                 .getAuthentication();
            Message message = new Message();
            message.setId(new MessageId(authentication.getName(),
                                        messageDto.getMessageReceiverUsername(),
                                        messageDto.getSendTime()));
            message.setText(messageDto.getText());
            messagesRepository.save(message);
        } else {
            throw new EntityNotFoundException(String.format("Не найден друг с именем %s",
                                                            messageDto.getMessageReceiverUsername()));
        }

    }

    public void sendMessage(MessageDto messageDto) {
        if (userService.hasFriend(messageDto.getMessageReceiverUsername())) {
            Authentication authentication = SecurityContextHolder.getContext()
                                                                 .getAuthentication();
            Message message = new Message();
            message.setId(new MessageId(authentication.getName(),
                                        messageDto.getMessageReceiverUsername(),
                                        messageDto.getSendTime()));
            message.setText(messageDto.getText());
            messagesRepository.save(message);
        } else {
            throw new EntityNotFoundException(String.format("Не найден друг с именем %s",
                                                            messageDto.getMessageReceiverUsername()));
        }

    }
}
