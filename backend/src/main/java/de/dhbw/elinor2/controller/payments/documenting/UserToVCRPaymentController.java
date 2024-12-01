package de.dhbw.elinor2.controller.payments.documenting;

import de.dhbw.elinor2.controller.payments.PaymentController;
import de.dhbw.elinor2.entities.UserToVCR;
import de.dhbw.elinor2.services.payments.documenting.UserToVCRService;
import de.dhbw.elinor2.utils.PaymentLight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/payments/doc/usertovcrs")
public class UserToVCRPaymentController extends PaymentController<PaymentLight, UserToVCR, UUID>
{
    @Autowired
    public UserToVCRPaymentController(UserToVCRService service)
    {
        super(service);
    }
}
