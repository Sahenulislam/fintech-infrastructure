package com.example.wallet_service.outbox;

import com.example.wallet_service.wallet.TransactionCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void save(Outbox outbox) {
        outboxRepository.save(outbox);
    }


    @Transactional
    public void save(String eventName, TransactionCreatedEvent event){

        try {
            String payload = objectMapper.writeValueAsString(event);

            Outbox outbox = Outbox.builder()
                    .eventType(eventName)
                    .status("DRAFT")
                    .createdAt(Instant.now())
                    .payload(payload)
                    .build();

            outboxRepository.save(outbox);
        }catch (Exception e){
            throw new  RuntimeException(e);
        }

    }
}
