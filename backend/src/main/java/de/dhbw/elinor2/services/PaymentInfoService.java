package de.dhbw.elinor2.services;

import de.dhbw.elinor2.entities.PaymentInfo;
import de.dhbw.elinor2.repositories.PaymentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentInfoService
{

    @Autowired
    private PaymentInfoRepository paymentInfoRepository;

    @Transactional
    public PaymentInfo createPaymentInfo(PaymentInfo paymentInfo)
    {
        return paymentInfoRepository.save(paymentInfo);
    }

    public Optional<PaymentInfo> getPaymentInfoById(UUID id)
    {
        return paymentInfoRepository.findById(id);
    }

    public List<PaymentInfo> getAllPaymentInfo()
    {
        return paymentInfoRepository.findAll();
    }

    @Transactional
    public Optional<PaymentInfo> updatePaymentInfo(UUID id, PaymentInfo paymentInfoDetails)
    {
        return paymentInfoRepository.findById(id).map(paymentInfo ->
        {
            paymentInfo.setName(paymentInfoDetails.getName());
            return paymentInfoRepository.save(paymentInfo);
        });
    }

    @Transactional
    public void deletePaymentInfo(UUID id)
    {
        paymentInfoRepository.deleteById(id);
    }
}
