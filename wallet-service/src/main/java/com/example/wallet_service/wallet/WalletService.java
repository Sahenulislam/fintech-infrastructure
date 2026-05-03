package com.example.wallet_service.wallet;

import com.example.wallet_service.kafka.Producer.WalletProducer;
import com.example.wallet_service.transaction.Transaction;
import com.example.wallet_service.transaction.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletProducer walletProducer;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void processTransaction(TransactionCreatedEvent event) {
        Transaction transaction = transactionRepository.findByTransactionId(event.getTransactionId());

        if (transaction == null) {
            transaction = Transaction.builder()
                    .status("PENDING")
                    .transactionId(event.getTransactionId())
                    .build();
            transactionRepository.save(transaction);
        } else if (List.of("FAILED", "SUCCESS").contains(transaction.getStatus())) {
            return;
        }

        try {
            Wallet sender = walletRepository.findByUserIdForUpdate(event.getSenderId())
                    .orElseThrow(() -> new RuntimeException("Sender wallet not found"));

            Wallet receiver = walletRepository.findByUserIdForUpdate(event.getReceiverId())
                    .orElseThrow(() -> new RuntimeException("Receiver wallet not found"));

            if (sender.getBalance().compareTo(event.getAmount()) < 0) {
                transaction.setStatus("FAILED");
                transactionRepository.save(transaction);

                event.setStatus("FAILED");
                walletProducer.sendTransactionEvent("transaction-completed", event);
                return;
            }

            sender.setBalance(sender.getBalance().subtract(event.getAmount()));
            sender.setLastTransactionId(event.getTransactionId());

            receiver.setBalance(receiver.getBalance().add(event.getAmount()));
            receiver.setLastTransactionId(event.getTransactionId());

            walletRepository.save(sender);
            walletRepository.save(receiver);
            transaction.setStatus("SUCCESS");
            transactionRepository.save(transaction);

            event.setStatus("SUCCESS");
            walletProducer.sendTransactionEvent("transaction-completed", event);

        } catch (Exception e) {
            transaction.setStatus("FAILED");
            transactionRepository.save(transaction);

            event.setStatus("FAILED");
            walletProducer.sendTransactionEvent("transaction-completed", event);

            throw e;
        }
    }

    @Transactional
    public Wallet create(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    @Transactional
    public Wallet update(Wallet wallet) {
        return walletRepository.save(wallet);
    }
}