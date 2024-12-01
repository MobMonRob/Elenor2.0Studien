package de.dhbw.elinor2.controller.payments.executing;

import de.dhbw.elinor2.controller.payments.PaymentController;
import de.dhbw.elinor2.entities.ExternToUser;
import de.dhbw.elinor2.services.payments.executiong.ExternToUserService;
import de.dhbw.elinor2.utils.PaymentOverVCRLight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/payments/exec/externtousers")
public class ExternToUserController extends PaymentController<PaymentOverVCRLight, ExternToUser, UUID>
{
    @Autowired
    public ExternToUserController(ExternToUserService service)
    {
        super(service);
    }
}
