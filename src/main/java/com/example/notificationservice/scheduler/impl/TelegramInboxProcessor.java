package com.example.notificationservice.scheduler.impl;

import com.example.notificationservice.model.TelegramInbox;
import com.example.notificationservice.repository.TelegramInboxRepository;
import com.example.notificationservice.scheduler.AbstractInboxScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TelegramInboxProcessor extends AbstractInboxScheduler<TelegramInbox> {
    private final TelegramInboxRepository repository;

    @Override
    protected List<TelegramInbox> findPending(int batchSize) {
        return repository.findByProcessedFalseOrderByCreatedAtAsc(PageRequest.of(0, batchSize));
    }

    @Override
    protected void processOne(TelegramInbox tg) {
        log.info("Обработано событие: Key: <{}>, Payload: <{}>, topic: <{}>",
                tg.getKey(), tg.getValue(), tg.getTopic());
    }

    @Override
    protected void saveAll(List<TelegramInbox> items) {
        repository.saveAll(items);
    }

    @Override
    protected void setProcessed(TelegramInbox item, boolean processed) {
        item.setProcessed(processed);
    }

    @Override
    protected void incrementAttempt(TelegramInbox item) {
        item.setAttempt(item.getAttempt() + 1);
    }

    @Override
    protected String getKey(TelegramInbox item) {
        return item.getKey();
    }
}
