package com.jayakulan.urbannest.controller;

import com.jayakulan.urbannest.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {

    @Value("${openai.api.key}")
    private String openAiKey;

    @Autowired
    private PropertyRepository propertyRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * POST /api/ai/chat
     * Body: { "message": "find cheap house in Colombo" }
     * Returns: { "reply": "Here are some properties..." }
     */
    @PostMapping("/chat")
    public ResponseEntity<Map<String, String>> chat(@RequestBody Map<String, String> body) {
        String userMessage = body.getOrDefault("message", "").trim();
        if (userMessage.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("reply", "Empty message."));
        }

        // Build a system prompt that includes real property data for context
        String propertyContext = buildPropertyContext();

        String systemPrompt = """
            You are UrbanNest AI, a smart property assistant for the UrbanNest rental platform.
            Help users find properties, answer rental questions, and give advice about renting.
            Be friendly, concise, and helpful. Format your responses clearly.
            
            Here is the current property listings data from our database:
            """ + propertyContext;

        // Build OpenAI request
        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("max_tokens", 500);
        requestBody.put("temperature", 0.7);

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", systemPrompt));
        messages.add(Map.of("role", "user", "content", userMessage));
        requestBody.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.openai.com/v1/chat/completions",
                entity,
                Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    @SuppressWarnings("unchecked")
                    Map<String, String> message = (Map<String, String>) choices.get(0).get("message");
                    String reply = message.getOrDefault("content", "No response.");
                    return ResponseEntity.ok(Map.of("reply", reply.trim()));
                }
            }
            return ResponseEntity.ok(Map.of("reply", "Sorry, I couldn't get a response. Please try again."));

        } catch (Exception e) {
            System.err.println("OpenAI API error: " + e.getMessage());
            return ResponseEntity.ok(Map.of("reply",
                "AI assistant is temporarily unavailable. Please try again later."));
        }
    }

    /** Summarize available properties from the DB to give AI real context */
    private String buildPropertyContext() {
        try {
            var properties = propertyRepository.findAll();
            if (properties.isEmpty()) return "No properties currently listed.";

            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (var p : properties) {
                if (count >= 20) break; // limit context size
                sb.append(String.format(
                    "- [ID:%d] %s | %s, %s | LKR %.0f/month | %d bed, %d bath | Status: %s%n",
                    p.getId(),
                    p.getTitle() != null ? p.getTitle() : "Property",
                    p.getCity() != null ? p.getCity() : "Unknown City",
                    p.getDistrict() != null ? p.getDistrict() : "",
                    p.getMonthlyRent() != null ? p.getMonthlyRent() : 0,
                    p.getBedrooms() != null ? p.getBedrooms() : 0,
                    p.getBathrooms() != null ? p.getBathrooms() : 0,
                    p.getAvailabilityStatus() != null ? p.getAvailabilityStatus() : "Unknown"
                ));
                count++;
            }
            return sb.toString();
        } catch (Exception e) {
            return "Property data unavailable.";
        }
    }
}
