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

        Wallet sender = repo.findByUserId(event.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender wallet not found"));

        Wallet receiver = repo.findByUserId(event.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver wallet not found"));

        if (sender.getBalance().compareTo(event.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance().subtract(event.getAmount()));
        receiver.setBalance(receiver.getBalance().add(event.getAmount()));

        repo.save(sender);
        repo.save(receiver);
    }

    @Transactional
    public Wallet create(Wallet wallet) {
        return repo.save(wallet);
    }

    @Transactional
    public Wallet update(Wallet wallet) {
        return repo.save(wallet);
    }
}