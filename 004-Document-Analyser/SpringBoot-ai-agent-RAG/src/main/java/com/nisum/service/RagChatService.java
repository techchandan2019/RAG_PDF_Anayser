package com.nisum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RagChatService {

    private final ChatClient chatClient;

    private final VectorStore vectorStore;
    RagChatService(ChatClient chatClient,VectorStore vectorStore){
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }
/*     public String askQuestion(
            UUID documentId,
            String question) {

        String filter =
                "fileId == '" + documentId + "'";

        QuestionAnswerAdvisor advisor =
                QuestionAnswerAdvisor.builder(vectorStore)
                        .searchRequest(
                                SearchRequest.builder()
                                        .topK(5)
                                        .similarityThreshold(0.65)
                                        .filterExpression(filter)
                                        .build()
                        )
                        .build();

        return chatClient.prompt()

                .system("""
                    You are a PDF document assistant.

                    Answer questions using only the
                    retrieved PDF context.

                    If the answer is not available in
                    the retrieved context, say:

                    "I could not find this information
                    in the uploaded PDF."

                    Do not invent facts.
                    """)

                .user(question)

                .advisors(advisor)

                .call()

                .content();

    }*/
    public String askQuestion(UUID documentId, String question) {

        // 1. Filter chunks by document ID
        String filter = "fileId == '" + documentId + "'";

        // 2. Retrieve relevant chunks from PGVector
        SearchRequest searchRequest = SearchRequest.builder()
                .query(question)
                .topK(5)
                .filterExpression(filter)
                .build();

        List<Document> documents =
                vectorStore.similaritySearch(searchRequest);

        // 3. Check whether relevant chunks were found
        if (documents == null || documents.isEmpty()) {
            return "I could not find this information in the uploaded PDF.";
        }

        // 4. Combine retrieved PDF chunks into context
        String context = documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));

        System.out.println("Retrieved context:\n" + context);

        // 5. Send context and question to Ollama
        return chatClient.prompt()
                .system("""
                    You are a PDF document assistant.

                    Answer the user's question using ONLY
                    the information in the provided PDF context.

                    If the answer is not available in the context,
                    say:
                    "I could not find this information in the uploaded PDF."

                    Do not make up information.
                    """)
                .user("""
                    PDF CONTEXT:
                    %s

                    QUESTION:
                    %s

                    Answer based only on the PDF context.
                    """.formatted(context, question))
                .call()
                .content();
    }
}