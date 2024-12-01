package de.dhbw.elinor2.services.payments;

import java.util.Optional;

public interface IPaymentService<PaymentPattern, Entity, Id>
{
    Entity create(PaymentPattern paymentPattern);

    Optional<Entity> findById(Id id);

    Iterable<Entity> findAll();

    Optional<Entity> update(Id id, PaymentPattern paymentPattern);

    void deleteById(Id id);

    boolean existsById(Id id);
}
