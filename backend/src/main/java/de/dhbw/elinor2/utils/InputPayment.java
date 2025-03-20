package de.dhbw.elinor2.utils;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class InputPayment
{
    private UUID senderId;

    private UUID receiverId;

    private BigDecimal amount = BigDecimal.ZERO;

    public InputPayment(UUID senderId, UUID receiverId, BigDecimal amount)
    {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
    }

    public InputPayment()
    {
    }
}
