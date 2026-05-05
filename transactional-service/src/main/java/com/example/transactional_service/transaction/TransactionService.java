package com.example.transactional_service.transaction;

import com.example.transactional_service.kafka.producer.TransactionProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final TransactionProducer transactionProducer;

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

        transactionProducer.sendTransactionEvent("transaction-created",event);

        return saved;
    }

    public void processTransaction(TransactionCreatedEvent event) {
        Transaction tx = transactionRepository.findById(event.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        tx.setStatus(event.getStatus());
        transactionRepository.save(tx);
    }
}