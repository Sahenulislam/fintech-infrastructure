package com.example.transactional_service.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCreatedEvent {
    private String transactionId;
    private Long senderId;
    private Long receiverId;
    private Double amount;
}