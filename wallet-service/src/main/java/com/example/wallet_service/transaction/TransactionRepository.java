package com.example.wallet_service.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = """
   select from transaction t
   where t.transaction_id = :transactionId
   and t.status in :status)
""", nativeQuery = true)
    Transaction findByTransactionIdAndStatus(Long transactionId, List<String> status);

    Transaction findByTransactionId(Long transactionId);
}
