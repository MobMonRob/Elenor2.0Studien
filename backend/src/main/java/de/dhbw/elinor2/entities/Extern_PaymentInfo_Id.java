package de.dhbw.elinor2.entities;

import java.io.Serializable;

public class Extern_PaymentInfo_Id implements Serializable
{
    private Extern extern;
    private PaymentInfo paymentInfo;

    public Extern_PaymentInfo_Id()
    {
    }

    public Extern_PaymentInfo_Id(Extern extern, PaymentInfo paymentInfo)
    {
        this.extern = extern;
        this.paymentInfo = paymentInfo;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Extern_PaymentInfo_Id that = (Extern_PaymentInfo_Id) obj;
        return extern.getId().equals(that.extern.getId())
                && paymentInfo.getId().equals(that.paymentInfo.getId());
    }

    @Override
    public int hashCode()
    {
        return extern.getId().hashCode() + paymentInfo.getId().hashCode();
    }
}
