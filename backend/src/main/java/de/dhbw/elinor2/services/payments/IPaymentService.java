package de.dhbw.elinor2.services.payments;

import de.dhbw.elinor2.utils.InputPayment;
import de.dhbw.elinor2.utils.OutputPayment;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

public interface IPaymentService<IP extends InputPayment, OP extends OutputPayment, T, ID>
{
    T create(IP paymentPattern, Jwt jwt);

    Optional<T> findById(ID id);

    Iterable<OP> findAll();

    Optional<T> update(ID id, IP paymentPattern, Jwt jwt);

    void deleteById(ID id, Jwt jwt);

    boolean existsById(ID id);
}
