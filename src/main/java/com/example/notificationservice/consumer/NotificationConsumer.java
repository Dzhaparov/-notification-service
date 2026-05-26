package com.example.notificationservice.consumer;

import com.example.notificationservice.model.EmailInbox;
import com.example.notificationservice.model.PushInbox;
import com.example.notificationservice.model.SmsInbox;
import com.example.notificationservice.model.TelegramInbox;
import com.example.notificationservice.repository.EmailInboxRepository;
import com.example.notificationservice.repository.PushInboxRepository;
import com.example.notificationservice.repository.SmsInboxRepository;
import com.example.notificationservice.repository.TelegramInboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final SmsInboxRepository smsInboxRepository;
    private final EmailInboxRepository emailInboxRepository;
    private final PushInboxRepository pushInboxRepository;
    private final TelegramInboxRepository telegramInboxRepository;

    @KafkaListener(topics = "telegram-events")
    public void consumeTelegram(ConsumerRecord<String, String> record) {
        String key = record.key();
        String value = record.value();

        if (telegramInboxRepository.existsByKeyAndValue(key, value)) {
            log.warn("Дубликат Telegram сообщения пропущен. Key: {}", key);
            return;
        }

        telegramInboxRepository.save(new TelegramInbox()
                .setKey(key)
                .setValue(value)
                .setTopic(record.topic())
                .setProcessed(false)
                .setAttempt(1));

        log.info("Сообщение сохранено в telegram_inbox. Key: {}", key);
    }

    @KafkaListener(topics = "push-events")
    public void consumePush(ConsumerRecord<String, String> record) {
        String key = record.key();
        String value = record.value();

        if (pushInboxRepository.existsByKeyAndValue(key, value)) {
            log.warn("Дубликат Push сообщения пропущен. Key: {}", key);
            return;
        }

        pushInboxRepository.save(new PushInbox()
                .setKey(key)
                .setValue(value)
                .setTopic(record.topic())
                .setProcessed(false)
                .setAttempt(1));

        log.info("Сообщение сохранено в push_inbox. Key: {}", key);
    }

    @KafkaListener(topics = "email-events")
    public void consumeEmail(ConsumerRecord<String, String> record) {
        String key = record.key();
        String value = record.value();

        if (emailInboxRepository.existsByKeyAndValue(key, value)) {
            log.warn("Дубликат Email сообщения пропущен. Key: {}", key);
            return;
        }

        emailInboxRepository.save(new EmailInbox()
                .setKey(key)
                .setValue(value)
                .setTopic(record.topic())
                .setProcessed(false)
                .setAttempt(1));

        log.info("Сообщение сохранено в email_inbox. Key: {}", key);
    }

    @KafkaListener(topics = "sms-events")
    public void consumeSms(ConsumerRecord<String, String> record) {
        String key = record.key();
        String value = record.value();

        if (smsInboxRepository.existsByKeyAndValue(key, value)) {
            log.warn("Дубликат SMS сообщения пропущен. Key: {}", key);
            return;
        }

        smsInboxRepository.save(new SmsInbox()
                .setKey(key)
                .setValue(value)
                .setTopic(record.topic())
                .setProcessed(false)
                .setAttempt(1));
        log.info("Сообщение сохранено в sms_inbox. Key: {}", key);
    }
}