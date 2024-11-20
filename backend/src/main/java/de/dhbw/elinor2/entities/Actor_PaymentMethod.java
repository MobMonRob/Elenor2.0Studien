package de.dhbw.elinor2.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="actor__payment_method")
@IdClass(Actor_PaymentMethod_Id.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Actor_PaymentMethod
{
    @Id
    @ManyToOne
    @JoinColumn(name = "actor_id", nullable = false)
    private Actor actor;

    @Id
    @ManyToOne
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private String identification;
}


