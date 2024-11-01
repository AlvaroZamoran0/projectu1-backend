package com.example.demo.Services;

import com.example.demo.Entities.DocumentEntity;
import com.example.demo.Repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class DocumentService {
    @Autowired
    DocumentRepository documentRepository;

    public List<DocumentEntity> getCreditDocuments(int id_credit){
        return documentRepository.findByCreditId(id_credit);
    }

    public DocumentEntity saveDocument(MultipartFile document, int id_credit){
        try {
            byte[] documentBytes = document.getBytes();
            List<DocumentEntity> documents = getCreditDocuments(id_credit);

            // Crear y guardar el nuevo documento
            DocumentEntity newDocument = new DocumentEntity(null, documentBytes, id_credit);
            return documentRepository.save(newDocument);
        } catch (IOException e) {
            throw new RuntimeException("Error al obtener los bytes del documento: " + e.getMessage(), e);
        }
    }

    public byte[] downloadDocument(Integer id_credit){
        List<DocumentEntity> documents = getCreditDocuments(id_credit);
        ByteArrayOutputStream filedocs = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(filedocs)) {
            int i = 1;
            for (DocumentEntity doc : documents) {
                ZipEntry zipEntry = new ZipEntry("document_" + i + "_credit_" + doc.getCreditId() + ".pdf");
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write(doc.getDocument()); // Escribe los bytes del documento en el ZIP
                zipOutputStream.closeEntry();
                i++;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al crear el archivo ZIP: " + e.getMessage(), e);
        }
        return filedocs.toByteArray();
    }
}
