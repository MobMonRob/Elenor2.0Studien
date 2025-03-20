package de.dhbw.elinor2.controller.payments.executing;

import de.dhbw.elinor2.controller.payments.PaymentController;
import de.dhbw.elinor2.entities.UserToExtern;
import de.dhbw.elinor2.services.payments.executiong.UserToExternService;
import de.dhbw.elinor2.utils.InputPaymentOverVcr;
import de.dhbw.elinor2.utils.OutputPaymentOverVcr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/payments/exec/usertoexterns")
public class UserToExternController extends PaymentController<InputPaymentOverVcr, OutputPaymentOverVcr, UserToExtern, UUID>
{
    @Autowired
    public UserToExternController(UserToExternService service)
    {
        super(service);
    }
}
