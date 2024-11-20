package de.dhbw.elinor2.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="payment_method")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class PaymentMethod
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "paymentMethod", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Actor_PaymentMethod> actor_PaymentMethod;
}
