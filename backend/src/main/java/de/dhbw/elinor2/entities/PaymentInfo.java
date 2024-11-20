package de.dhbw.elinor2.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="payment_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PaymentInfo
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "paymentInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member_PaymentInfo> member_PaymentInfo;

    @OneToMany(mappedBy = "paymentInfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Extern_PaymentInfo> extern_PaymentInfo;
}
