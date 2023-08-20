package org.temkarus0070.efmsocialmedia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.temkarus0070.efmsocialmedia.dto.DialogDto;
import org.temkarus0070.efmsocialmedia.dto.MessageDto;
import org.temkarus0070.efmsocialmedia.entities.Message;
import org.temkarus0070.efmsocialmedia.entities.MessageId;

import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<Message, MessageId> {

    @Query(value = """
                   SELECT distinct new org.temkarus0070.efmsocialmedia.dto.DialogDto(case when m.id.senderUsername=:currentUsername
                    then m.id.destinationUsername 
                    else m.id.senderUsername END ) 
                   from Message m
                    where m.id.senderUsername=:currentUsername or m.id.destinationUsername=:currentUsername
                   """)
    List<DialogDto> getAllActiveUserDialogs(@Param("currentUsername") String currentUsername);

    @Query(value = """
                   SELECT new org.temkarus0070.efmsocialmedia.dto.MessageDto(m.id.senderUsername,m.id.destinationUsername,m.id.creationTime,m.text)
                   from Message m
                    where m.id.senderUsername=:currentUsername and m.id.destinationUsername=:friendUsername OR
                    m.id.destinationUsername=:currentUsername and m.id.senderUsername=:friendUsername
                   """)
    List<MessageDto> getAllMessagesWithFriend(@Param("currentUsername") String currentUsername,
                                              @Param("friendUsername") String friendUsername);
}
