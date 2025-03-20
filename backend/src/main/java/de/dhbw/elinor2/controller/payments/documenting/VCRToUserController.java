package de.dhbw.elinor2.controller.payments.documenting;

import de.dhbw.elinor2.controller.payments.PaymentController;
import de.dhbw.elinor2.entities.VCRToUser;
import de.dhbw.elinor2.services.payments.documenting.VCRToUserService;
import de.dhbw.elinor2.utils.InputPayment;
import de.dhbw.elinor2.utils.OutputPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/payments/doc/vcrtousers")
public class VCRToUserController extends PaymentController<InputPayment, OutputPayment, VCRToUser, UUID>
{
    @Autowired
    public VCRToUserController(VCRToUserService service)
    {
        super(service);
    }
}
