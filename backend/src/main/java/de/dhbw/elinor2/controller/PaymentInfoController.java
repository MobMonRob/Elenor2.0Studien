package de.dhbw.elinor2.controller;

import de.dhbw.elinor2.entities.PaymentInfo;
import de.dhbw.elinor2.services.IGenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/paymentinfos")
public class PaymentInfoController extends GenericController<PaymentInfo, UUID>
{
    @Autowired
    public PaymentInfoController(IGenericService<PaymentInfo, UUID> service)
    {
        super(service);
    }
}
