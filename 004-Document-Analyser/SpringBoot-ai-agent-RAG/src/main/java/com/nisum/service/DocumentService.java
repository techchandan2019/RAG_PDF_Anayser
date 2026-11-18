package com.nisum.service;

import com.nisum.dto.DocumentStatus;
import com.nisum.entity.DocumentEntity;
import com.nisum.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepository repository;

    private final PdfProcessingService pdfProcessingService;

    private final VectorStoreService vectorStoreService;
    DocumentService(DocumentRepository repository, PdfProcessingService pdfProcessingService, VectorStoreService vectorStoreService) {
        this.repository = repository;
        this.pdfProcessingService = pdfProcessingService;
        this.vectorStoreService = vectorStoreService;

    }

    public String uploadPdf(MultipartFile file)
            throws IOException {

        if (file.isEmpty() ||
                !file.getOriginalFilename()
                        .toLowerCase()
                        .endsWith(".pdf")) {

            throw new IllegalArgumentException(
                    "Please upload a valid PDF"
            );
        }


        DocumentEntity entity = new DocumentEntity();

        entity.setFileName(file.getOriginalFilename());
        entity.setContentType(file.getContentType());
        entity.setFileSize(file.getSize());
        entity.setStatus(DocumentStatus.PROCESSING);
        entity.setUploadedAt(LocalDateTime.now());

        // Save first so Hibernate generates the ID
        entity = repository.save(entity);

        String documentId = entity.getId().toString();

        // Now use the same ID everywhere
        entity.setStoredFileName(documentId + ".pdf");


        try {

            byte[] bytes = file.getBytes();

            List<Document> chunks =
                    pdfProcessingService.processPdf(
                            bytes,
                            documentId,
                            file.getOriginalFilename()
                    );

            vectorStoreService.saveDocuments(chunks);

            entity.setStatus(DocumentStatus.COMPLETED);

            repository.save(entity);

            return documentId;

        } catch (Exception exception) {

            entity.setStatus(DocumentStatus.FAILED);

            repository.save(entity);

            throw new RuntimeException(
                    "PDF processing failed", exception
            );
        }
    }
}