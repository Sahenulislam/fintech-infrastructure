package com.example.wallet_service.outbox;


import com.example.wallet_service.kafka.Producer.WalletProducer;
import com.example.wallet_service.wallet.TransactionCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {
    private final OutboxRepository outboxRepository;
    private final OutboxService outboxService;
    private final WalletProducer walletProducer;

    @Scheduled(fixedDelay = 5000)
    public void publish() {
        List<Outbox> outboxList = outboxRepository.findAllByStatus(PageRequest.of(0, 100), "DRAFT");

        for (Outbox outbox : outboxList) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                TransactionCreatedEvent event = objectMapper.readValue(outbox.getPayload(), TransactionCreatedEvent.class);
                walletProducer.sendTransactionEvent(outbox.getEventType(), event);

                outbox.setStatus("SENT");
                outboxService.save(outbox);

            } catch (Exception e) {
                outbox.setStatus("FAILED");
                outboxService.save(outbox);
            }
        }

    }
}
