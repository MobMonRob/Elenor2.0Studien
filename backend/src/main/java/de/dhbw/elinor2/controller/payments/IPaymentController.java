package de.dhbw.elinor2.controller.payments;

import de.dhbw.elinor2.utils.InputPayment;
import de.dhbw.elinor2.utils.OutputPayment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

public interface IPaymentController<IP extends InputPayment, OP extends OutputPayment, T, ID>
{
    ResponseEntity<OP> create(IP paymentPattern, Jwt jwt);

    ResponseEntity<T> findById(ID id);

    ResponseEntity<Iterable<OP>> findAll();

    ResponseEntity<OP> update(ID id, IP paymentPattern, Jwt jwt);

    ResponseEntity<Void> deleteById(ID id, Jwt jwt);
}
