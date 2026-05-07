package com.example.transactional_service.outbox;


import com.example.transactional_service.kafka.producer.TransactionProducer;
import com.example.transactional_service.transaction.TransactionCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {
    private final OutboxRepository outboxRepository;
    private final OutboxService outboxService;
    private final TransactionProducer transactionProducer;

    @Scheduled(fixedDelay = 5000)
    public void publish() {
        List<Outbox> outboxList = outboxRepository.findAllByStatus(PageRequest.of(0, 100), "DRAFT");

        for (Outbox outbox : outboxList) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                TransactionCreatedEvent event = objectMapper.readValue(outbox.getPayload(), TransactionCreatedEvent.class);
                transactionProducer.sendTransactionEvent(outbox.getEventType(), event);

                outbox.setStatus("SENT");
                outboxService.save(outbox);

            } catch (Exception e) {
                outbox.setStatus("FAILED");
                outboxService.save(outbox);
            }
        }

    }
}
