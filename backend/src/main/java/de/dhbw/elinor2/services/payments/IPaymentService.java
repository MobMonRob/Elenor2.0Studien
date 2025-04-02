package de.dhbw.elinor2.services.payments;

import de.dhbw.elinor2.utils.InputPayment;
import de.dhbw.elinor2.utils.OutputPayment;
import org.springframework.security.oauth2.jwt.Jwt;

public interface IPaymentService<IP extends InputPayment, OP extends OutputPayment, ID>
{
    OP create(IP paymentPattern, Jwt jwt);

    OP findById(ID id);

    Iterable<OP> findAll();

    OP update(ID id, IP paymentPattern, Jwt jwt);

    void deleteById(ID id, Jwt jwt);

    boolean existsById(ID id);
}
