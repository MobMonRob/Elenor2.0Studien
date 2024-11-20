package de.dhbw.elinor2.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class Payment
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();
}
