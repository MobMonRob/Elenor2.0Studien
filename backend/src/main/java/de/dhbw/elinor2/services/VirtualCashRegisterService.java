package de.dhbw.elinor2.services;

import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class VirtualCashRegisterService extends GenericService<VirtualCashRegister, UUID>
{
    @Autowired
    public VirtualCashRegisterService(VirtualCashRegisterRepository repository)
    {
        super(repository);
    }
}
