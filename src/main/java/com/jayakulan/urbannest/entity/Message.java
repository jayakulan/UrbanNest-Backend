package com.jayakulan.urbannest.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long receiverId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Column(nullable = false)
    private Boolean isRead = false;

    // Composite key: min(senderId,receiverId)_max(senderId,receiverId)
    // Groups a conversation between two users
    @Column(nullable = false)
    private String conversationKey;

    // Denormalized for display convenience
    private String senderName;

    @PrePersist
    public void prePersist() {
        if (this.sentAt == null) {
            this.sentAt = LocalDateTime.now();
        }
        if (this.conversationKey == null && this.senderId != null && this.receiverId != null) {
            long a = Math.min(senderId, receiverId);
            long b = Math.max(senderId, receiverId);
            this.conversationKey = a + "_" + b;
        }
        if (this.isRead == null) this.isRead = false;
    }

    public Message() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

    public Boolean getIsRead() { return isRead; }
    public void setIsRead(Boolean isRead) { this.isRead = isRead; }

    public String getConversationKey() { return conversationKey; }
    public void setConversationKey(String conversationKey) { this.conversationKey = conversationKey; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
}
