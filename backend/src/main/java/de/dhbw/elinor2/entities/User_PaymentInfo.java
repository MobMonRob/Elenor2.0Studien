package de.dhbw.elinor2.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_payment_info")
@IdClass(User_PaymentInfo_Id.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User_PaymentInfo
{
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "payment_info_id", nullable = false)
    private PaymentInfo paymentInfo;

    @Column(nullable = false)
    private String paymentAddress;
}


