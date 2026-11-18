package com.nisum.dto;


import java.util.UUID;

public record ChatRequest(
         UUID documentId,
         String question
) {}