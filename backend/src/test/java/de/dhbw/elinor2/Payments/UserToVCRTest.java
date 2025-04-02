package de.dhbw.elinor2.Payments;

import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.UserToVCR;
import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import de.dhbw.elinor2.repositories.payments.UserToVCRRepository;
import de.dhbw.elinor2.services.payments.documenting.UserToVCRService;
import de.dhbw.elinor2.utils.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserToVCRTest extends GenericTest<InputPayment, UserToVCR, OutputPaymentOverVcr, UUID>
{
    @Autowired
    private UserToVCRRepository userToVCRRepository;

    @Autowired
    private UserToVCRService userToVCRService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VirtualCashRegisterRepository virtualCashRegisterRepository;

    private User user;

    private VirtualCashRegister virtualCashRegister;

    @Override
    @AfterEach
    public void deleteTestData()
    {
        userToVCRRepository.deleteAll();
        userRepository.deleteAll();
        virtualCashRegisterRepository.deleteAll();
    }

    @Override
    public String getObjectAssertionIdentificationSavedEntity(UserToVCR userToVCR)
    {
        return userToVCR.getUser().getId().toString() +
                userToVCR.getVirtualCashRegister().getId().toString() +
                userToVCR.getAmount().intValue();
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
    public TestObject<InputPayment, UserToVCR, OutputPaymentOverVcr, UUID> initTestObject()
    {
        TestObject<InputPayment, UserToVCR, OutputPaymentOverVcr, UUID> testObject = new TestObject<>();
        testObject.setEntityClass(OutputPaymentOverVcr.class);
        testObject.setEntityArrayClass(OutputPaymentOverVcr[].class);
        testObject.setRepository(userToVCRRepository);
        testObject.setBaseUrl("http://localhost:8080/api/payments");

        user = DefaultUser.getDefaultUser();
        user = userRepository.save(user);

        virtualCashRegister = new VirtualCashRegister();
        virtualCashRegister.setName("testVCR");
        virtualCashRegister = virtualCashRegisterRepository.save(virtualCashRegister);

        UserToVCR userToVCR = new UserToVCR();
        userToVCR.setUser(user);
        userToVCR.setVirtualCashRegister(virtualCashRegister);
        userToVCR.setAmount(BigDecimal.valueOf(100));
        userToVCRService.executePayment(userToVCR, DefaultUser.getJwtToken());
        userToVCR = userToVCRRepository.save(userToVCR);

        testObject.setInitSavedEntity(userToVCR);
        testObject.setInitSavedEntityId(userToVCR.getId());

        InputPayment updatedInputPaymentLight = new InputPayment();
        updatedInputPaymentLight.setSenderId(user.getId());
        updatedInputPaymentLight.setReceiverId(virtualCashRegister.getId());
        updatedInputPaymentLight.setAmount(BigDecimal.valueOf(200));
        testObject.setUpdateEntity(updatedInputPaymentLight);

        InputPayment newInputPaymentLight = new InputPayment();
        newInputPaymentLight.setSenderId(user.getId());
        newInputPaymentLight.setReceiverId(virtualCashRegister.getId());
        newInputPaymentLight.setAmount(BigDecimal.valueOf(300));
        testObject.setNewEntity(newInputPaymentLight);

        return testObject;
    }

    @Override
    @Test
    public void postRequest()
    {
        super.postRequest();
        VirtualCashRegister virtualCashRegisterResult = virtualCashRegisterRepository.findById(this.virtualCashRegister.getId()).orElseThrow();
        User userResult = userRepository.findById(this.user.getId()).orElseThrow();

        Assertions.assertEquals(400, virtualCashRegisterResult.getBalance().intValue());
        Assertions.assertEquals(-400, userResult.getBalance().intValue());
    }

    @Override
    @Test
    public void putRequest()
    {
        super.putRequest();
        VirtualCashRegister virtualCashRegisterResult = virtualCashRegisterRepository.findById(this.virtualCashRegister.getId()).orElseThrow();
        User userResult = userRepository.findById(this.user.getId()).orElseThrow();

        Assertions.assertEquals(200, virtualCashRegisterResult.getBalance().intValue());
        Assertions.assertEquals(-200, userResult.getBalance().intValue());
    }

    @Override
    @Test
    public void deleteRequest()
    {
        super.deleteRequest();
        VirtualCashRegister virtualCashRegisterResult = virtualCashRegisterRepository.findById(this.virtualCashRegister.getId()).orElseThrow();
        User userResult = userRepository.findById(this.user.getId()).orElseThrow();

        Assertions.assertEquals(0, virtualCashRegisterResult.getBalance().intValue());
        Assertions.assertEquals(0, userResult.getBalance().intValue());
    }
}
