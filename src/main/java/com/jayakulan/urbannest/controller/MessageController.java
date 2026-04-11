package com.jayakulan.urbannest.controller;

import com.jayakulan.urbannest.entity.Message;
import com.jayakulan.urbannest.entity.Role;
import com.jayakulan.urbannest.repository.BookingRepository;
import com.jayakulan.urbannest.repository.MessageRepository;
import com.jayakulan.urbannest.repository.PropertyRepository;
import com.jayakulan.urbannest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired private MessageRepository messageRepository;
    @Autowired private UserRepository    userRepository;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private PropertyRepository propertyRepository;
    @Autowired private SimpMessagingTemplate messagingTemplate;

    // ── WebSocket: receive, save, broadcast ───────────────────────────────────
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload Message message) {
        if (message.getSenderName() == null) {
            userRepository.findById(message.getSenderId())
                .ifPresent(u -> message.setSenderName(u.getFullName()));
        }
        Message saved = messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/chat/" + saved.getConversationKey(), saved);
    }

    // ── REST: load history for a conversation ─────────────────────────────────
    @GetMapping("/conversation/{key}")
    public ResponseEntity<List<Message>> getConversation(@PathVariable String key) {
        return ResponseEntity.ok(messageRepository.findByConversationKeyOrderBySentAtAsc(key));
    }

    // ── REST: smart inbox — booking partners + existing chat partners ─────────
    @GetMapping("/inbox/{userId}")
    public ResponseEntity<List<Map<String, Object>>> getInbox(@PathVariable Long userId) {

        // Ordered map: partnerId → inbox entry (preserves insertion order)
        LinkedHashMap<Long, Map<String, Object>> inbox = new LinkedHashMap<>();

        // 1. Existing message conversations (most recent first)
        List<Message> allMsgs = messageRepository.findAllMessagesInvolvingUser(userId);
        for (Message m : allMsgs) {
            Long partnerId = m.getSenderId().equals(userId) ? m.getReceiverId() : m.getSenderId();
            if (!inbox.containsKey(partnerId)) {
                long unread = messageRepository.findByReceiverIdAndIsReadFalse(userId)
                    .stream().filter(x -> x.getSenderId().equals(partnerId)).count();
                Map<String, Object> entry = buildEntry(partnerId, userId,
                    m.getContent(), m.getSentAt().toString(), (int) unread);
                if (!entry.isEmpty()) inbox.put(partnerId, entry);
            }
        }

        // 2. Booking-based partners (add if not already in inbox)
        userRepository.findById(userId).ifPresent(user -> {
            if (user.getRole() == Role.TENANT) {
                // Tenant sees: owner of each property they booked
                bookingRepository.findByTenantId(userId).forEach(booking ->
                    propertyRepository.findById(booking.getPropertyId()).ifPresent(prop -> {
                        Long ownerId = prop.getOwnerId();
                        if (ownerId != null && !inbox.containsKey(ownerId)) {
                            Map<String, Object> entry = buildEntry(ownerId, userId,
                                "Booked: " + prop.getTitle(), null, 0);
                            if (!entry.isEmpty()) inbox.put(ownerId, entry);
                        }
                    })
                );
            } else if (user.getRole() == Role.OWNER) {
                // Owner sees: tenants who booked their properties
                propertyRepository.findByOwnerId(userId).forEach(prop ->
                    bookingRepository.findByPropertyId(prop.getId()).forEach(booking -> {
                        Long tenantId = booking.getTenantId();
                        if (!inbox.containsKey(tenantId)) {
                            Map<String, Object> entry = buildEntry(tenantId, userId,
                                "Booking: " + prop.getTitle(), null, 0);
                            if (!entry.isEmpty()) inbox.put(tenantId, entry);
                        }
                    })
                );
            }
        });

        return ResponseEntity.ok(new ArrayList<>(inbox.values()));
    }

    /** Build a single inbox entry for a partner. Returns empty map if partner not found. */
    private Map<String, Object> buildEntry(Long partnerId, Long userId,
                                            String lastMsg, String lastTime, int unread) {
        Map<String, Object> entry = new LinkedHashMap<>();
        userRepository.findById(partnerId).ifPresent(partner -> {
            long a = Math.min(userId, partnerId);
            long b = Math.max(userId, partnerId);
            entry.put("partnerId",        partnerId);
            entry.put("partnerName",      partner.getFullName());
            entry.put("partnerRole",      partner.getRole().name());
            entry.put("conversationKey",  a + "_" + b);
            entry.put("lastMessage",      lastMsg != null ? lastMsg : "");
            entry.put("lastMessageTime",  lastTime != null ? lastTime : "");
            entry.put("unreadCount",      unread);
        });
        return entry;
    }

    // ── REST: mark conversation as read ──────────────────────────────────────
    @PatchMapping("/conversation/{key}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable String key, @RequestParam Long userId) {
        messageRepository.findByConversationKeyOrderBySentAtAsc(key).stream()
            .filter(m -> m.getReceiverId().equals(userId) && !Boolean.TRUE.equals(m.getIsRead()))
            .forEach(m -> { m.setIsRead(true); messageRepository.save(m); });
        return ResponseEntity.ok().build();
    }
}
