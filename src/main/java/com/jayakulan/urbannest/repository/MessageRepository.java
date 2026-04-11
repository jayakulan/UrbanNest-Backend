package com.jayakulan.urbannest.repository;

import com.jayakulan.urbannest.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Load all messages in a conversation, ordered by time
    List<Message> findByConversationKeyOrderBySentAtAsc(String conversationKey);

    // Unread messages for a user
    List<Message> findByReceiverIdAndIsReadFalse(Long receiverId);

    // All unique conversation partners for a user (people they have chatted with)
    @Query("SELECT DISTINCT m FROM Message m WHERE m.senderId = :userId OR m.receiverId = :userId ORDER BY m.sentAt DESC")
    List<Message> findAllMessagesInvolvingUser(@Param("userId") Long userId);
}
