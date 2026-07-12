package com.validator.academia.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "verification_logs")
public class VerificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = true)
    private Document document;

    @Column(name = "checked_hash", nullable = false)
    private String checkedHash;

    @Column(name = "verification_time", nullable = false)
    private LocalDateTime verificationTime;

    @Column(nullable = false)
    private String result;

    public VerificationLog() {
    }

    @PrePersist
    protected void onCreate() {
        if (verificationTime == null) {
            verificationTime = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getCheckedHash() {
        return checkedHash;
    }

    public void setCheckedHash(String checkedHash) {
        this.checkedHash = checkedHash;
    }

    public LocalDateTime getVerificationTime() {
        return verificationTime;
    }

    public void setVerificationTime(LocalDateTime verificationTime) {
        this.verificationTime = verificationTime;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
