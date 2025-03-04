package de.dhbw.elinor2.services.payments;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public interface IPaymentService<PaymentPattern, Entity, Id>
{
    Entity create(PaymentPattern paymentPattern, Jwt jwt);

    Optional<Entity> findById(Id id);

    Iterable<Entity> findAll();

    Optional<Entity> update(Id id, PaymentPattern paymentPattern, Jwt jwt);

    void deleteById(Id id, Jwt jwt);

    boolean existsById(Id id);
}
