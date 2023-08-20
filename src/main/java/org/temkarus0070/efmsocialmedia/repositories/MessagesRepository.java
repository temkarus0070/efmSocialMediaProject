package org.temkarus0070.efmsocialmedia.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.temkarus0070.efmsocialmedia.entities.Message;
import org.temkarus0070.efmsocialmedia.entities.MessageId;

@Repository
public interface MessagesRepository extends JpaRepository<Message, MessageId> {}
