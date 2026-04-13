package com.example.wallet_service.wallet;

import jakarta.transaction.Transaction;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository repo;

    @Transactional
    public void processTransaction(TransactionCreatedEvent event) {

        Wallet sender = repo.findById(event.getSenderId()).orElseThrow();
        Wallet receiver = repo.findById(event.getReceiverId()).orElseThrow();

        if (sender.getBalance().compareTo(event.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(event.getAmount()));
        receiver.setBalance(receiver.getBalance().add(event.getAmount()));

        repo.save(sender);
        repo.save(receiver);
    }
}