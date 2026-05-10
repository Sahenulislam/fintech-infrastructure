package com.example.wallet_service.kafka.Producer;


import com.example.wallet_service.wallet.TransactionCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletProducer {

    private final KafkaTemplate<String, TransactionCreatedEvent> kafkaTemplate;


    public void sendTransactionEvent(String string, TransactionCreatedEvent event) {
        kafkaTemplate.send(string, event);
    }

}
