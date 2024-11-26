package de.dhbw.elinor2.entities;

import java.io.Serializable;

public class User_PaymentInfo_Id implements Serializable
{
    private User user;
    private PaymentInfo paymentInfo;

    public User_PaymentInfo_Id()
    {
    }

    public User_PaymentInfo_Id(User user, PaymentInfo paymentInfo)
    {
        this.user = user;
        this.paymentInfo = paymentInfo;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User_PaymentInfo_Id that = (User_PaymentInfo_Id) obj;
        return user.getId().equals(that.user.getId())
                && paymentInfo.getId().equals(that.paymentInfo.getId());
    }

    @Override
    public int hashCode()
    {
        return user.getId().hashCode() + paymentInfo.getId().hashCode();
    }
}
