package de.dhbw.elinor2.services.payments;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public interface IPaymentService<PaymentPattern, T, ID>
{
    T create(PaymentPattern paymentPattern, Jwt jwt);

    Optional<T> findById(ID id);

    Iterable<T> findAll();

    Optional<T> update(ID id, PaymentPattern paymentPattern, Jwt jwt);

    void deleteById(ID id, Jwt jwt);

    boolean existsById(ID id);
}
