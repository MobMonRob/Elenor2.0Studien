package de.dhbw.elinor2.utils;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class OutputPayment
{
    private PaymentType paymentType;
    private UUID transactionId;
    private BigDecimal amount;
    private LocalDateTime timestamp;
    private TransactionEntity sender;
    private TransactionEntity receiver;

    public OutputPayment(PaymentType paymentType, UUID transactionId, BigDecimal amount, LocalDateTime timestamp, TransactionEntity sender, TransactionEntity receiver)
    {
        this.paymentType = paymentType;
        this.transactionId = transactionId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.sender = sender;
        this.receiver = receiver;
    }
}

