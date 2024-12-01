package de.dhbw.elinor2.services;

import de.dhbw.elinor2.entities.Extern;
import de.dhbw.elinor2.entities.Extern_PaymentInfo;
import de.dhbw.elinor2.entities.PaymentInfo;
import de.dhbw.elinor2.repositories.ExternRepository;
import de.dhbw.elinor2.repositories.Extern_PaymentInfoRepository;
import de.dhbw.elinor2.repositories.PaymentInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ExternService extends GenericService<Extern, UUID>
{

    @Autowired
    private ExternRepository externRepository;

    @Autowired
    private PaymentInfoRepository paymentInfoRepository;

    @Autowired
    private Extern_PaymentInfoRepository externPaymentInfoRepository;

    @Autowired
    public ExternService(ExternRepository repository)
    {
        super(repository);
    }

    @Transactional
    public Iterable<Extern_PaymentInfo> getExternPaymentInfoFields(UUID externId)
    {
        return externPaymentInfoRepository.findByExternId(externId);
    }

    @Transactional
    public Optional<Extern_PaymentInfo> getExternPaymentInfoField(UUID externId, UUID paymentInfoId)
    {
        return externPaymentInfoRepository.findByExternIdAndPaymentInfoId(externId, paymentInfoId);
    }

    @Transactional
    public Extern_PaymentInfo createExternPaymentInfo(UUID externId, UUID paymentInfoId, String paymentAddress)
    {
        Extern extern = externRepository.findById(externId).orElseThrow(()
                -> new IllegalArgumentException("Extern not found"));
        PaymentInfo paymentInfo = paymentInfoRepository.findById(paymentInfoId).orElseThrow(()
                -> new IllegalArgumentException("PaymentInfo not found"));

        Extern_PaymentInfo externPaymentInfo = new Extern_PaymentInfo();
        externPaymentInfo.setExtern(extern);
        externPaymentInfo.setPaymentInfo(paymentInfo);
        externPaymentInfo.setPaymentAddress(paymentAddress);
        externPaymentInfoRepository.save(externPaymentInfo);
        return externPaymentInfo;
    }

    @Transactional
    public Optional<Extern_PaymentInfo> updateExternPaymentInfo(UUID externId, UUID paymentInfoId, String paymentAddress)
    {
        return externPaymentInfoRepository.findByExternIdAndPaymentInfoId(externId, paymentInfoId).map(externPaymentInfo ->
        {
            externPaymentInfo.setPaymentAddress(paymentAddress);
            return externPaymentInfoRepository.save(externPaymentInfo);
        });
    }

    @Transactional
    public void deleteExternPaymentInfo(UUID externId, UUID paymentInfoId)
    {
        externPaymentInfoRepository.deleteByExternIdAndPaymentInfoId(externId, paymentInfoId);
    }
}
