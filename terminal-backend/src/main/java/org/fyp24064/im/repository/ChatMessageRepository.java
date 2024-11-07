package org.fyp24064.im.repository;

import org.fyp24064.im.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>  {
    List<ChatMessage> findAllByChatRoomId(Long id);
}