package com.example.wallet_service.wallet;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCreatedEvent {
    private String transactionId;
    private Long senderId;
    private Long receiverId;
    private BigDecimal amount;
}