package com.example.transactional_service.kafka.consumer;


import com.example.transactional_service.transaction.TransactionCreatedEvent;
import com.example.transactional_service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionalConsumer {
    private final TransactionService transactionService;

    @KafkaListener(topics = "transaction-completed", groupId = "transactional-group")
    public void consumeTransactionCreatedEvent(TransactionCreatedEvent event) {
        transactionService.processTransaction(event);
    }
}
