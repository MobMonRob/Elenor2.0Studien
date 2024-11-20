package de.dhbw.elinor2.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="member_payment_info")
@IdClass(Member_PaymentInfo_Id.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member_PaymentInfo
{
    @Id
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Id
    @ManyToOne
    @JoinColumn(name = "payment_info_id", nullable = false)
    private PaymentInfo paymentInfo;

    @Column(nullable = false)
    private String identification;
}


