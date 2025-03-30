package de.dhbw.elinor2.services;

import de.dhbw.elinor2.entities.PaymentInfo;
import de.dhbw.elinor2.repositories.PaymentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentInfoService extends GenericService<PaymentInfo, UUID>
{

    @Autowired
    public PaymentInfoService(PaymentInfoRepository repository)
    {
        super(repository);
    }

    @Override
    protected PaymentInfo updateEntity(PaymentInfo entity, PaymentInfo updatedEntity)
    {
        entity.setName(updatedEntity.getName());
        return entity;
    }
}
