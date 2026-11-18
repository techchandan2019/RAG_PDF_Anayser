package com.nisum.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PdfProcessingService {

    public List<Document> processPdf(
            byte[] pdfBytes,
            String fileId,
            String fileName) {

        Resource resource =
                new ByteArrayResource(pdfBytes);

        PagePdfDocumentReader reader =
                new PagePdfDocumentReader(resource);

        List<Document> documents = reader.read();

        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(800)
                .withMinChunkSizeChars(350)
                .withMinChunkLengthToEmbed(5)
                .withMaxNumChunks(10000)
                .withKeepSeparator(true)
                .build();

        List<Document> chunks =
                splitter.split(documents);

        for (Document chunk : chunks) {

            chunk.getMetadata().put(
                    "fileId", fileId
            );

            chunk.getMetadata().put(
                    "fileName", fileName
            );
        }

        return chunks;
    }
}