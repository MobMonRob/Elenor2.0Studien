package de.dhbw.elinor2.controller.payments;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

public interface IPaymentController<PaymentPattern, Entity, Id>
{
    ResponseEntity<Entity> create(PaymentPattern paymentPattern, Jwt jwt);

    ResponseEntity<Entity> findById(Id id);

    ResponseEntity<Iterable<Entity>> findAll();

    ResponseEntity<Entity> update(Id id, PaymentPattern paymentPattern, Jwt jwt);

    ResponseEntity<Void> deleteById(Id id, Jwt jwt);
}
