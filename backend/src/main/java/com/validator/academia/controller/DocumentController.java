package com.validator.academia.controller;

import com.validator.academia.dto.UploadResponse;
import com.validator.academia.model.Document;
import com.validator.academia.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.validator.academia.dto.VerificationResponse;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadDocument(
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        try {
            // authentication.getName() should return the email of the authenticated user
            String email = authentication.getName();
            
            Document savedDocument = documentService.uploadDocument(file, email);
            
            return ResponseEntity.ok(new UploadResponse(
                    true,
                    savedDocument.getId(),
                    savedDocument.getHash(),
                    "Document uploaded successfully."
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new UploadResponse(false, null, null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new UploadResponse(false, null, null, "An error occurred during upload."));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<VerificationResponse> verifyDocument(@RequestParam("file") MultipartFile file) {
        try {
            VerificationResponse response = documentService.verifyDocument(file);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new VerificationResponse(false, e.getMessage(), null, null, null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new VerificationResponse(false, "An error occurred during verification.", null, null, null));
        }
    }
}
