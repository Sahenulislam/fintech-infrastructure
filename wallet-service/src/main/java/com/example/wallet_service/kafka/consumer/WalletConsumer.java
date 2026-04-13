package com.example.wallet_service.kafka.consumer;

import com.example.wallet_service.wallet.TransactionCreatedEvent;
import com.example.wallet_service.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletConsumer {

    private final WalletService walletService;

    @KafkaListener(topics = "transaction-created", groupId = "wallet-group")
    public void consume(TransactionCreatedEvent tx) {
        walletService.processTransaction(tx);
    }
}