package com.validator.academia.repository;

import com.validator.academia.model.VerificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationLogRepository extends JpaRepository<VerificationLog, Long> {
}
