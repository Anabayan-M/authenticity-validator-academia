package com.validator.academia.dto;

public class UploadResponse {
    private boolean success;
    private Long documentId;
    private String hash;
    private String message;

    public UploadResponse(boolean success, Long documentId, String hash, String message) {
        this.success = success;
        this.documentId = documentId;
        this.hash = hash;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public Long getDocumentId() { return documentId; }
    public void setDocumentId(Long documentId) { this.documentId = documentId; }

    public String getHash() { return hash; }
    public void setHash(String hash) { this.hash = hash; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
