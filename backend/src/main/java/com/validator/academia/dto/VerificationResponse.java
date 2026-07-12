package com.validator.academia.dto;

import java.time.LocalDateTime;

public class VerificationResponse {
    private boolean verified;
    private String message;
    private String documentName;
    private String uploadedBy;
    private LocalDateTime uploadDate;

    public VerificationResponse(boolean verified, String message, String documentName, String uploadedBy, LocalDateTime uploadDate) {
        this.verified = verified;
        this.message = message;
        this.documentName = documentName;
        this.uploadedBy = uploadedBy;
        this.uploadDate = uploadDate;
    }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getDocumentName() { return documentName; }
    public void setDocumentName(String documentName) { this.documentName = documentName; }

    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }

    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
}
