package com.example.demo.Controllers;

import com.example.demo.Entities.DocumentEntity;
import com.example.demo.Services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/document")
@CrossOrigin
public class DocumentController {
    @Autowired
    DocumentService documentService;

    @PostMapping("/save_doc")
    public ResponseEntity<DocumentEntity> saveDocument(
        @RequestParam("document") MultipartFile document,
        @RequestParam("id_credit") int id_credit){
        DocumentEntity doc = documentService.saveDocument(document, id_credit);
        return ResponseEntity.ok(doc);
    }

    @GetMapping("/download/{id_credit}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Integer id_credit){
        byte[] zipBytes = documentService.downloadDocument(id_credit);
        String zipFileName = "documents_credit_" + id_credit + ".zip";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipFileName)
                .body(zipBytes);
    }
}