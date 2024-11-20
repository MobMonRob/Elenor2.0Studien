package de.dhbw.elinor2.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="actor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Actor
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String country;

    private String city;

    private String street;

    private int houseNumber;

    private int postalCode;

    @OneToMany(mappedBy = "actor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Actor_PaymentMethod> actor_PaymentMethods;
}
