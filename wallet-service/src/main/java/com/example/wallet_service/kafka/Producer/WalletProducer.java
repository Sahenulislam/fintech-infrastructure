package com.example.wallet_service.kafka.Producer;


import com.example.wallet_service.kafka.consumer.WalletConsumer;
import com.example.wallet_service.wallet.TransactionCreatedEvent;
import com.example.wallet_service.wallet.WalletService;
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
