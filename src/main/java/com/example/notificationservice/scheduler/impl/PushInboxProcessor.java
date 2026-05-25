package com.example.notificationservice.scheduler.impl;

import com.example.notificationservice.model.PushInbox;
import com.example.notificationservice.repository.PushInboxRepository;
import com.example.notificationservice.scheduler.AbstractInboxScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PushInboxProcessor extends AbstractInboxScheduler<PushInbox> {
    private final PushInboxRepository repository;

    @Override
    protected List<PushInbox> findPending(int batchSize) {
        return repository.findByProcessedFalseOrderByCreatedAtAsc(PageRequest.of(0, batchSize));
    }

    @Override
    protected void processOne(PushInbox push) {
        log.info("Обработано событие: Key: <{}>, Payload: <{}>, topic: <{}>",
                push.getKey(), push.getValue(), push.getTopic());
    }

    @Override
    protected void saveAll(List<PushInbox> items) {
        repository.saveAll(items);
    }

    @Override
    protected void setProcessed(PushInbox item, boolean processed) {
        item.setProcessed(processed);
    }

    @Override
    protected void incrementAttempt(PushInbox item) {
        item.setAttempt(item.getAttempt() + 1);
    }

    @Override
    protected String getKey(PushInbox item) {
        return item.getKey();
    }
}