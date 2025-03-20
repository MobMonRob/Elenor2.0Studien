package de.dhbw.elinor2.utils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class OutputPaymentOverVcr extends OutputPayment
{
    private TransactionEntity overVcr;
    public OutputPaymentOverVcr(PaymentType paymentType,
                                UUID transactionId,
                                BigDecimal amount,
                                LocalDateTime timestamp,
                                TransactionEntity sender,
                                TransactionEntity receiver,
                                TransactionEntity overVcr)
    {
        super(paymentType, transactionId, amount, timestamp, sender, receiver);
        this.overVcr = overVcr;
    }

    public TransactionEntity getOverVcr()
    {
        return overVcr;
    }

    public void setOverVcr(TransactionEntity overVcr)
    {
        this.overVcr = overVcr;
    }
}
