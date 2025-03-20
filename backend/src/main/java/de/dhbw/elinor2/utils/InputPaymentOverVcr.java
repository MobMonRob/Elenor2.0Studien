package de.dhbw.elinor2.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class InputPaymentOverVcr extends InputPayment
{
    private UUID vcrId;
}
