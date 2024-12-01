package de.dhbw.elinor2.controller;

import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.services.IGenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/virtualcashregisters")
public class VirtualCashRegisterController extends GenericController<VirtualCashRegister, UUID>
{
    @Autowired
    public VirtualCashRegisterController(IGenericService<VirtualCashRegister, UUID> service)
    {
        super(service);
    }
}
