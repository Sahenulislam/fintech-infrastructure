package com.example.transactional_service.kafka.producer;

import com.example.transactional_service.transaction.TransactionCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendTransactionEvent(TransactionCreatedEvent event) {
        kafkaTemplate.send("transaction-created", event);
    }
}