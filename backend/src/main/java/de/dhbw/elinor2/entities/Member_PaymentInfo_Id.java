package de.dhbw.elinor2.entities;

import java.io.Serializable;

public class Member_PaymentInfo_Id implements Serializable
{
    private Member member;
    private PaymentInfo paymentInfo;

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Member_PaymentInfo_Id that = (Member_PaymentInfo_Id) obj;
        return member.getId().equals(that.member.getId())
                && paymentInfo.getId().equals(that.paymentInfo.getId());
    }

    @Override
    public int hashCode()
    {
        return member.getId().hashCode() + paymentInfo.getId().hashCode();
    }
}
