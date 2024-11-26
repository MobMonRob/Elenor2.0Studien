package de.dhbw.elinor2.services;

import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VirtualCashRegisterService
{

    @Autowired
    private VirtualCashRegisterRepository virtualCashRegisterRepository;

    @Transactional
    public VirtualCashRegister createVirtualCashRegister(VirtualCashRegister virtualCashRegister)
    {
        return virtualCashRegisterRepository.save(virtualCashRegister);
    }

    public Optional<VirtualCashRegister> getVirtualCashRegisterById(UUID id)
    {
        return virtualCashRegisterRepository.findById(id);
    }

    public List<VirtualCashRegister> getAllVirtualCashRegisters()
    {
        return virtualCashRegisterRepository.findAll();
    }

    @Transactional
    public Optional<VirtualCashRegister> updateVirtualCashRegister(UUID id, VirtualCashRegister virtualCashRegisterDetails)
    {
        return virtualCashRegisterRepository.findById(id).map(virtualCashRegister ->
        {
            virtualCashRegister.setName(virtualCashRegisterDetails.getName());
            virtualCashRegister.setBalance(virtualCashRegisterDetails.getBalance());
            return virtualCashRegisterRepository.save(virtualCashRegister);
        });
    }

    @Transactional
    public void deleteVirtualCashRegister(UUID id)
    {
        virtualCashRegisterRepository.deleteById(id);
    }
}
