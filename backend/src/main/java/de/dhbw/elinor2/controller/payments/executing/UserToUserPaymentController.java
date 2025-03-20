package de.dhbw.elinor2.controller.payments.executing;

import de.dhbw.elinor2.controller.payments.PaymentController;
import de.dhbw.elinor2.entities.UserToUser;
import de.dhbw.elinor2.services.payments.executiong.UserToUserService;
import de.dhbw.elinor2.utils.InputPayment;
import de.dhbw.elinor2.utils.OutputPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/payments/exec/usertousers")
public class UserToUserPaymentController extends PaymentController<InputPayment, OutputPayment, UserToUser, UUID>
{
    @Autowired
    public UserToUserPaymentController(UserToUserService service)
    {
        super(service);
    }
}
