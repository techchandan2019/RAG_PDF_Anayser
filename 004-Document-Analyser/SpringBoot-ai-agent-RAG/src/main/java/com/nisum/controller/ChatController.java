package com.nisum.controller;

import com.nisum.dto.ChatRequest;
import com.nisum.dto.ChatResponse;
import com.nisum.service.RagChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final RagChatService ragChatService;

    ChatController(RagChatService ragChatService) {
        this.ragChatService = ragChatService;
    }
    @PostMapping
    public ResponseEntity<ChatResponse> chat(
            @RequestBody ChatRequest request) {

        String answer =
                ragChatService.askQuestion(
                        request.documentId(),
                        request.question()
                );

        return ResponseEntity.ok(
                new ChatResponse(
                        request.documentId(),
                        request.question(),
                        answer
                )
        );
    }
}
