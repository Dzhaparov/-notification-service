package com.example.notificationservice.scheduler.impl;

import com.example.notificationservice.model.SmsInbox;
import com.example.notificationservice.repository.SmsInboxRepository;
import com.example.notificationservice.scheduler.AbstractInboxScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SmsInboxProcessor extends AbstractInboxScheduler<SmsInbox> {
    private final SmsInboxRepository repository;

    @Override
    protected List<SmsInbox> findPending(int batchSize) {
        return repository.findByProcessedFalseOrderByCreatedAtAsc(PageRequest.of(0, batchSize));
    }

    @Override
    protected void processOne(SmsInbox sms) {
        log.info("Обработано событие: Key: <{}>, Payload: <{}>, topic: <{}>",
                sms.getKey(), sms.getValue(), sms.getTopic());
    }

    @Override
    protected void saveAll(List<SmsInbox> items) {
        repository.saveAll(items);
    }

    @Override
    protected void setProcessed(SmsInbox item, boolean processed) {
        item.setProcessed(processed);
    }

    @Override
    protected void incrementAttempt(SmsInbox item) {
        item.setAttempt(item.getAttempt() + 1);
    }

    @Override
    protected String getKey(SmsInbox item) {
        return item.getKey();
    }
}