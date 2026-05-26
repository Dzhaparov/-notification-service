package com.example.notificationservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "email_inbox")
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class EmailInbox {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String key;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String value;

    @Column(nullable = false)
    private boolean processed = false;

    @Column(nullable = false)
    private int attempt = 1;
}
