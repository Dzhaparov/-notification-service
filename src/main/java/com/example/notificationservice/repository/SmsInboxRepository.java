package com.example.notificationservice.repository;

import com.example.notificationservice.model.SmsInbox;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SmsInboxRepository extends JpaRepository<SmsInbox, UUID> {

    boolean existsByKeyAndValue(String key, String value);

    List<SmsInbox> findByProcessedFalseOrderByCreatedAtAsc(Pageable pageable);
}