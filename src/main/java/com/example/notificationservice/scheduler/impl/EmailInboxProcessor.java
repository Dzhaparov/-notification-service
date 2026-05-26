package com.example.notificationservice.scheduler.impl;

import com.example.notificationservice.model.EmailInbox;
import com.example.notificationservice.repository.EmailInboxRepository;
import com.example.notificationservice.scheduler.AbstractInboxScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailInboxProcessor extends AbstractInboxScheduler<EmailInbox> {
    private final EmailInboxRepository repository;

    @Override
    protected List<EmailInbox> findPending(int batchSize) {
        return repository.findByProcessedFalseOrderByCreatedAtAsc(PageRequest.of(0, batchSize));
    }

    @Override
    protected void processOne(EmailInbox email) {
        log.info("Обработано событие: Key: <{}>, Payload: <{}>, topic: <{}>",
                email.getKey(), email.getValue(), email.getTopic());
    }

    @Override
    protected void saveAll(List<EmailInbox> items) {
        repository.saveAll(items);
    }

    @Override
    protected void setProcessed(EmailInbox item, boolean processed) {
        item.setProcessed(processed);
    }

    @Override
    protected void incrementAttempt(EmailInbox item) {
        item.setAttempt(item.getAttempt() + 1);
    }

    @Override
    protected String getKey(EmailInbox item) {
        return item.getKey();
    }
}
