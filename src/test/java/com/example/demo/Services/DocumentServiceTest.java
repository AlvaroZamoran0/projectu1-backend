package com.example.demo.Services;

import com.example.demo.Entities.DocumentEntity;
import com.example.demo.Repositories.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentServiceTest {

    @InjectMocks
    private DocumentService documentService;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private MultipartFile mockFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCreditDocuments() {
        // Datos de prueba
        int idCredit = 1;
        DocumentEntity doc1 = new DocumentEntity(1L, new byte[]{1, 2, 3}, idCredit);
        DocumentEntity doc2 = new DocumentEntity(2L, new byte[]{4, 5, 6}, idCredit);
        when(documentRepository.findByCreditId(idCredit)).thenReturn(Arrays.asList(doc1, doc2));

        // Ejecución del método
        List<DocumentEntity> documents = documentService.getCreditDocuments(idCredit);

        // Verificaciones
        assertEquals(2, documents.size());
        assertEquals(doc1, documents.get(0));
        assertEquals(doc2, documents.get(1));
        verify(documentRepository).findByCreditId(idCredit);
    }

    @Test
    void testSaveDocument() throws IOException {
        // Datos de prueba
        int idCredit = 1;
        byte[] documentBytes = new byte[]{1, 2, 3};
        when(mockFile.getBytes()).thenReturn(documentBytes);
        when(documentRepository.save(any(DocumentEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Ejecución del método
        DocumentEntity savedDocument = documentService.saveDocument(mockFile, idCredit);

        // Verificaciones
        assertNotNull(savedDocument);
        assertArrayEquals(documentBytes, savedDocument.getDocument());
        assertEquals(idCredit, savedDocument.getCreditId());
        verify(mockFile).getBytes();
        verify(documentRepository).save(any(DocumentEntity.class));
    }

    @Test
    void testDownloadDocument() {
        // Datos de prueba
        int idCredit = 1;
        DocumentEntity doc1 = new DocumentEntity(1L, new byte[]{1, 2, 3}, idCredit);
        DocumentEntity doc2 = new DocumentEntity(2L, new byte[]{4, 5, 6}, idCredit);
        when(documentRepository.findByCreditId(idCredit)).thenReturn(Arrays.asList(doc1, doc2));

        // Ejecución del método
        byte[] zipFile = documentService.downloadDocument(idCredit);

        // Verificaciones
        assertNotNull(zipFile);
        // Verificar que el tamaño del archivo ZIP es correcto (en este caso, esto es un ejemplo simplificado)
        assertTrue(zipFile.length > 0);
        verify(documentRepository).findByCreditId(idCredit);
    }

    @Test
    void testSaveDocumentThrowsException() throws IOException {
        // Datos de prueba
        int idCredit = 1;
        when(mockFile.getBytes()).thenThrow(new IOException("Error al obtener bytes"));

        // Ejecución y verificación
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            documentService.saveDocument(mockFile, idCredit);
        });
        assertEquals("Error al obtener los bytes del documento: Error al obtener bytes", thrown.getMessage());
    }

    @Test
    void testDownloadDocumentThrowsException() {
        // Datos de prueba
        int idCredit = 1;
        when(documentRepository.findByCreditId(idCredit)).thenThrow(new RuntimeException("Error al obtener documentos"));

        // Ejecución y verificación
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            documentService.downloadDocument(idCredit);
        });
        assertEquals("Error al obtener documentos", thrown.getMessage());
    }
}
