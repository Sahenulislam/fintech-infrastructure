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
                saved.getId().toString(),
                saved.getSenderId(),
                saved.getReceiverId(),
                saved.getAmount()
        );

        transactionProducer.sendTransactionEvent(event);

        return saved;
    }
}