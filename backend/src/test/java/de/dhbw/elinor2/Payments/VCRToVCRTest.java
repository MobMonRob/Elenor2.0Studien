package de.dhbw.elinor2.Payments;

import de.dhbw.elinor2.entities.VCRToVCR;
import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import de.dhbw.elinor2.repositories.payments.VCRToVCRRepository;
import de.dhbw.elinor2.services.payments.documenting.VCRToVCRService;
import de.dhbw.elinor2.utils.GenericTest;
import de.dhbw.elinor2.utils.PaymentLight;
import de.dhbw.elinor2.utils.TestObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class VCRToVCRTest extends GenericTest<PaymentLight, VCRToVCR, UUID>
{
    @Autowired
    private VCRToVCRRepository vcrToVCRRepository;

    @Autowired
    private VCRToVCRService vcrToVCRService;

    @Autowired
    private VirtualCashRegisterRepository virtualCashRegisterRepository;

    private VirtualCashRegister sender;

    private VirtualCashRegister receiver;

    @Override
    @AfterEach
    public void deleteTestData()
    {
        vcrToVCRRepository.deleteAll();
        virtualCashRegisterRepository.deleteAll();
    }

    @Override
    public String getObjectAssertionIdentificationSavedEntity(VCRToVCR vcrToVCR)
    {
        return vcrToVCR.getSender().getId().toString() +
                vcrToVCR.getReceiver().getId().toString() +
                vcrToVCR.getAmount().intValue();
    }

    @Override
    public String getObjectAssertionIdentificationReceivedEntity(PaymentLight paymentLight)
    {
        return paymentLight.getSenderId().toString() +
                paymentLight.getReceiverId().toString() +
                paymentLight.getAmount().intValue();
    }

    @Override
    public TestObject<PaymentLight, VCRToVCR, UUID> initTestObject()
    {
        TestObject<PaymentLight, VCRToVCR, UUID> testObject = new TestObject<>();
        testObject.setEntityClass(VCRToVCR.class);
        testObject.setEntityArrayClass(VCRToVCR[].class);
        testObject.setRepository(vcrToVCRRepository);
        testObject.setBaseUrl("http://localhost:8080/api/payments/doc/vcrtovcrs");

        sender = new VirtualCashRegister();
        sender.setName("Sender");
        sender = virtualCashRegisterRepository.save(sender);

        receiver = new VirtualCashRegister();
        receiver.setName("Receiver");
        receiver = virtualCashRegisterRepository.save(receiver);

        PaymentLight paymentLight = new PaymentLight();
        paymentLight.setSenderId(sender.getId());
        paymentLight.setReceiverId(receiver.getId());
        paymentLight.setAmount(new BigDecimal(100));
        VCRToVCR vcrToVCR = vcrToVCRService.create(paymentLight);
        testObject.setInitSavedEntity(vcrToVCR);
        testObject.setInitSavedEntityId(vcrToVCR.getId());

        PaymentLight updatedPaymentLight = new PaymentLight();
        updatedPaymentLight.setSenderId(sender.getId());
        updatedPaymentLight.setReceiverId(receiver.getId());
        updatedPaymentLight.setAmount(new BigDecimal(200));
        testObject.setUpdateEntity(updatedPaymentLight);

        PaymentLight newPaymentLight = new PaymentLight();
        newPaymentLight.setSenderId(sender.getId());
        newPaymentLight.setReceiverId(receiver.getId());
        newPaymentLight.setAmount(new BigDecimal(300));
        testObject.setNewEntity(newPaymentLight);

        return testObject;
    }

    @Override
    @Test
    public void postRequest()
    {
        super.postRequest();
        VirtualCashRegister sender = virtualCashRegisterRepository.findById(this.sender.getId()).orElseThrow();
        VirtualCashRegister receiver = virtualCashRegisterRepository.findById(this.receiver.getId()).orElseThrow();

        Assertions.assertEquals(-400, sender.getBalance().intValue());
        Assertions.assertEquals(400, receiver.getBalance().intValue());
    }

    @Override
    @Test
    public void putRequest()
    {
        super.putRequest();
        VirtualCashRegister sender = virtualCashRegisterRepository.findById(this.sender.getId()).orElseThrow();
        VirtualCashRegister receiver = virtualCashRegisterRepository.findById(this.receiver.getId()).orElseThrow();

        Assertions.assertEquals(-200, sender.getBalance().intValue());
        Assertions.assertEquals(200, receiver.getBalance().intValue());
    }

    @Override
    @Test
    public void deleteRequest()
    {
        super.deleteRequest();
        VirtualCashRegister sender = virtualCashRegisterRepository.findById(this.sender.getId()).orElseThrow();
        VirtualCashRegister receiver = virtualCashRegisterRepository.findById(this.receiver.getId()).orElseThrow();

        Assertions.assertEquals(0, sender.getBalance().intValue());
        Assertions.assertEquals(0, receiver.getBalance().intValue());
    }
}
