package de.dhbw.elinor2.controller.payments;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

public interface IPaymentController<PaymentPattern, T, ID>
{
    ResponseEntity<T> create(PaymentPattern paymentPattern, Jwt jwt);

    ResponseEntity<T> findById(ID id);

    ResponseEntity<Iterable<T>> findAll();

    ResponseEntity<T> update(ID id, PaymentPattern paymentPattern, Jwt jwt);

    ResponseEntity<Void> deleteById(ID id, Jwt jwt);
}
