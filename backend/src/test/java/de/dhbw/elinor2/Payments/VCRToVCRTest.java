package de.dhbw.elinor2.Payments;

import de.dhbw.elinor2.entities.VCRToVCR;
import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import de.dhbw.elinor2.repositories.payments.VCRToVCRRepository;
import de.dhbw.elinor2.services.payments.documenting.VCRToVCRService;
import de.dhbw.elinor2.utils.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class VCRToVCRTest extends GenericTest<InputPayment, VCRToVCR, OutputPaymentOverVcr, UUID>
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
    public String getObjectAssertionIdentificationReceivedEntity(InputPayment inputPaymentLight)
    {
        return inputPaymentLight.getSenderId().toString() +
                inputPaymentLight.getReceiverId().toString() +
                inputPaymentLight.getAmount().intValue();
    }

    @Override
    public String getObjectAssertionIdentificationSendEntity(OutputPaymentOverVcr outputPaymentOverVcr)
    {
        return outputPaymentOverVcr.getSender().getEntityId().toString() +
                outputPaymentOverVcr.getReceiver().getEntityId().toString() +
                outputPaymentOverVcr.getAmount().intValue();
    }

    @Override
    public TestObject<InputPayment, VCRToVCR, OutputPaymentOverVcr, UUID> initTestObject()
    {
        TestObject<InputPayment, VCRToVCR, OutputPaymentOverVcr, UUID> testObject = new TestObject<>();
        testObject.setEntityClass(OutputPaymentOverVcr.class);
        testObject.setEntityArrayClass(OutputPaymentOverVcr[].class);
        testObject.setRepository(vcrToVCRRepository);
        testObject.setBaseUrl("http://localhost:8080/api/payments");

        sender = new VirtualCashRegister();
        sender.setName("Sender");
        sender = virtualCashRegisterRepository.save(sender);

        receiver = new VirtualCashRegister();
        receiver.setName("Receiver");
        receiver = virtualCashRegisterRepository.save(receiver);

        VCRToVCR vcrToVCR = new VCRToVCR();
        vcrToVCR.setSender(sender);
        vcrToVCR.setReceiver(receiver);
        vcrToVCR.setAmount(new BigDecimal(100));
        vcrToVCRService.executePayment(vcrToVCR, DefaultUser.getJwtToken());
        vcrToVCR = vcrToVCRRepository.save(vcrToVCR);

        testObject.setInitSavedEntity(vcrToVCR);
        testObject.setInitSavedEntityId(vcrToVCR.getId());

        InputPayment updatedInputPaymentLight = new InputPayment();
        updatedInputPaymentLight.setSenderId(sender.getId());
        updatedInputPaymentLight.setReceiverId(receiver.getId());
        updatedInputPaymentLight.setAmount(new BigDecimal(200));
        testObject.setUpdateEntity(updatedInputPaymentLight);

        InputPayment newInputPaymentLight = new InputPayment();
        newInputPaymentLight.setSenderId(sender.getId());
        newInputPaymentLight.setReceiverId(receiver.getId());
        newInputPaymentLight.setAmount(new BigDecimal(300));
        testObject.setNewEntity(newInputPaymentLight);

        return testObject;
    }

    @Override
    @Test
    public void postRequest()
    {
        super.postRequest();
        VirtualCashRegister senderResult = virtualCashRegisterRepository.findById(this.sender.getId()).orElseThrow();
        VirtualCashRegister receiverResult = virtualCashRegisterRepository.findById(this.receiver.getId()).orElseThrow();

        Assertions.assertEquals(-400, senderResult.getBalance().intValue());
        Assertions.assertEquals(400, receiverResult.getBalance().intValue());
    }

    @Override
    @Test
    public void putRequest()
    {
        super.putRequest();
        VirtualCashRegister senderResult = virtualCashRegisterRepository.findById(this.sender.getId()).orElseThrow();
        VirtualCashRegister receiverResult = virtualCashRegisterRepository.findById(this.receiver.getId()).orElseThrow();

        Assertions.assertEquals(-200, senderResult.getBalance().intValue());
        Assertions.assertEquals(200, receiverResult.getBalance().intValue());
    }

    @Override
    @Test
    public void deleteRequest()
    {
        super.deleteRequest();
        VirtualCashRegister senderResult = virtualCashRegisterRepository.findById(this.sender.getId()).orElseThrow();
        VirtualCashRegister receiverResult = virtualCashRegisterRepository.findById(this.receiver.getId()).orElseThrow();

        Assertions.assertEquals(0, senderResult.getBalance().intValue());
        Assertions.assertEquals(0, receiverResult.getBalance().intValue());
    }
}
