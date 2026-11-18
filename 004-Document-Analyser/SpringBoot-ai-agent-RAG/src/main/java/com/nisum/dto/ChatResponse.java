package com.nisum.dto;

import java.util.UUID;

public record ChatResponse(
        UUID documentId,
        String question,
        String answer
) {}