package com.example.transactional_service.transaction;

import com.example.transactional_service.kafka.producer.TransactionProducer;
import com.example.transactional_service.outbox.Outbox;
import com.example.transactional_service.outbox.OutboxService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final OutboxService outboxService;
    private final ObjectMapper objectMapper;

    private final TransactionProducer transactionProducer;

    @Transactional
    public Transaction createTransaction(Transaction tx) {
        tx.setStatus("PENDING");

        Transaction saved = transactionRepository.save(tx);

        TransactionCreatedEvent event = new TransactionCreatedEvent(
                saved.getId(),
                saved.getSenderId(),
                saved.getReceiverId(),
                saved.getAmount(),
                saved.getStatus()
        );

        try {

            String payload = objectMapper.writeValueAsString(event);
            Outbox outbox = new Outbox();

            outbox.setEventType("transaction-created");
            outbox.setStatus("DRAFT");
            outbox.setPayload(payload);

            outboxService.save(outbox);

            return saved;

        }catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    public void processTransaction(TransactionCreatedEvent event) {
        Transaction tx = transactionRepository.findById(event.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        tx.setStatus(event.getStatus());
        transactionRepository.save(tx);
    }
}