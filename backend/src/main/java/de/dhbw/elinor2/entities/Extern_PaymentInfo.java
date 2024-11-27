package de.dhbw.elinor2.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "extern_payment_info")
@IdClass(Extern_PaymentInfo_Id.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Extern_PaymentInfo
{
    @Id
    @ManyToOne
    @JoinColumn(name = "extern_id", nullable = false)
    private Extern extern;

    @Id
    @ManyToOne
    @JoinColumn(name = "payment_info_id", nullable = false)
    private PaymentInfo paymentInfo;

    @Column(nullable = false)
    private String paymentAddress;
}


