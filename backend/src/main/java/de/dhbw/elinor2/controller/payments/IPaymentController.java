package de.dhbw.elinor2.controller.payments;

import org.springframework.http.ResponseEntity;

public interface IPaymentController<PaymentPattern, Entity, Id>
{
    ResponseEntity<Entity> create(PaymentPattern paymentPattern);

    ResponseEntity<Entity> findById(Id id);

    ResponseEntity<Iterable<Entity>> findAll();

    ResponseEntity<Entity> update(Id id, PaymentPattern paymentPattern);

    ResponseEntity<Void> deleteById(Id id);
}
