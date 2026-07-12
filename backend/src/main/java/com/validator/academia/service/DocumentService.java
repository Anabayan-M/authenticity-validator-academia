package com.validator.academia.service;

import com.validator.academia.model.Document;
import com.validator.academia.model.User;
import com.validator.academia.repository.DocumentRepository;
import com.validator.academia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.validator.academia.dto.VerificationResponse;
import com.validator.academia.model.VerificationLog;
import com.validator.academia.repository.VerificationLogRepository;
import java.util.Optional;

@Service
public class DocumentService {

    private final String UPLOAD_DIR = "uploads/";

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationLogRepository verificationLogRepository;

    public Document uploadDocument(MultipartFile file, String email) throws IOException, NoSuchAlgorithmException {
        // Validate file type
        if (!"application/pdf".equals(file.getContentType())) {
            throw new IllegalArgumentException("Only PDF files are allowed.");
        }

        // Calculate SHA-256 hash
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(file.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        String fileHash = hexString.toString();

        // Check if document already exists
        if (documentRepository.findByHash(fileHash).isPresent()) {
            throw new IllegalArgumentException("Document already exists in the system.");
        }

        // Store file locally
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        Files.write(filePath, file.getBytes());

        // Get user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Save metadata to database
        Document document = new Document();
        document.setDocumentName(file.getOriginalFilename());
        document.setFilePath(filePath.toAbsolutePath().toString());
        document.setHash(fileHash);
        document.setUploadedBy(user);
        document.setStatus("PENDING_VERIFICATION");

        return documentRepository.save(document);
    }

    public VerificationResponse verifyDocument(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        // Validate file type
        if (!"application/pdf".equals(file.getContentType())) {
            throw new IllegalArgumentException("Only PDF files are allowed.");
        }

        // Calculate SHA-256 hash
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(file.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        String fileHash = hexString.toString();

        Optional<Document> existingDocument = documentRepository.findByHash(fileHash);
        
        VerificationLog log = new VerificationLog();
        log.setCheckedHash(fileHash);

        if (existingDocument.isPresent()) {
            Document doc = existingDocument.get();
            log.setDocument(doc);
            log.setResult("Authentic Document");
            verificationLogRepository.save(log);

            return new VerificationResponse(
                    true,
                    "Authentic Document",
                    doc.getDocumentName(),
                    doc.getUploadedBy().getName(),
                    doc.getUploadDate()
            );
        } else {
            log.setDocument(null);
            log.setResult("Document Not Verified");
            verificationLogRepository.save(log);

            return new VerificationResponse(
                    false,
                    "Document Not Verified",
                    null,
                    null,
                    null
            );
        }
    }
}
