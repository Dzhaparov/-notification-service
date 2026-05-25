package com.example.notificationservice.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public abstract class AbstractInboxScheduler<T> implements NotificationInboxScheduler {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${inbox.batch-size}")
    protected int batchSize;

    protected abstract List<T> findPending(int batchSize);
    protected abstract void processOne(T item) throws Exception;
    protected abstract void saveAll(List<T> items);
    protected abstract void setProcessed(T item, boolean processed);
    protected abstract void incrementAttempt(T item);
    protected abstract String getKey(T item);

    @Scheduled(fixedDelayString = "${inbox.delay-ms}")
    @Override
    @Transactional
    public void process() {
        List<T> pending = findPending(batchSize);
        if (pending.isEmpty()) {
            return;
        }
        log.info("Найдено {} необработанных записей", pending.size());
        for (T item : pending) {
            try {
                processOne(item);
                setProcessed(item, true);
            } catch (Exception e) {
                incrementAttempt(item);
                log.error("Ошибка обработки {}", getKey(item), e);
            }
        }
        saveAll(pending);
    }
}
