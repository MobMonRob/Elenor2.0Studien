package de.dhbw.elinor2.Payments;

import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.UserToVCR;
import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import de.dhbw.elinor2.repositories.payments.UserToVCRRepository;
import de.dhbw.elinor2.services.payments.documenting.UserToVCRService;
import de.dhbw.elinor2.utils.DefaultUser;
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
public class UserToVCRTest extends GenericTest<PaymentLight, UserToVCR, UUID>
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
    public String getObjectAssertionIdentificationReceivedEntity(PaymentLight paymentLight)
    {
        return paymentLight.getSenderId().toString() +
                paymentLight.getReceiverId().toString() +
                paymentLight.getAmount().intValue();
    }

    @Override
    public TestObject<PaymentLight, UserToVCR, UUID> initTestObject()
    {
        TestObject<PaymentLight, UserToVCR, UUID> testObject = new TestObject<>();
        testObject.setEntityClass(UserToVCR.class);
        testObject.setEntityArrayClass(UserToVCR[].class);
        testObject.setRepository(userToVCRRepository);
        testObject.setBaseUrl("http://localhost:8080/api/payments/doc/usertovcrs");

        user = DefaultUser.getDefaultUser();
        user = userRepository.save(user);

        virtualCashRegister = new VirtualCashRegister();
        virtualCashRegister.setName("testVCR");
        virtualCashRegister = virtualCashRegisterRepository.save(virtualCashRegister);


        PaymentLight paymentLight = new PaymentLight();
        paymentLight.setSenderId(user.getId());
        paymentLight.setReceiverId(virtualCashRegister.getId());
        paymentLight.setAmount(BigDecimal.valueOf(100));
        UserToVCR userToVCR = userToVCRService.create(paymentLight, DefaultUser.getJwtToken());
        testObject.setInitSavedEntity(userToVCR);
        testObject.setInitSavedEntityId(userToVCR.getId());

        PaymentLight updatedPaymentLight = new PaymentLight();
        updatedPaymentLight.setSenderId(user.getId());
        updatedPaymentLight.setReceiverId(virtualCashRegister.getId());
        updatedPaymentLight.setAmount(BigDecimal.valueOf(200));
        testObject.setUpdateEntity(updatedPaymentLight);

        PaymentLight newPaymentLight = new PaymentLight();
        newPaymentLight.setSenderId(user.getId());
        newPaymentLight.setReceiverId(virtualCashRegister.getId());
        newPaymentLight.setAmount(BigDecimal.valueOf(300));
        testObject.setNewEntity(newPaymentLight);

        return testObject;
    }

    @Override
    @Test
    public void postRequest()
    {
        super.postRequest();
        VirtualCashRegister virtualCashRegister = virtualCashRegisterRepository.findById(this.virtualCashRegister.getId()).orElseThrow();
        User user = userRepository.findById(this.user.getId()).orElseThrow();

        Assertions.assertEquals(400, virtualCashRegister.getBalance().intValue());
        Assertions.assertEquals(400, user.getDebt().intValue());
    }

    @Override
    @Test
    public void putRequest()
    {
        super.putRequest();
        VirtualCashRegister virtualCashRegister = virtualCashRegisterRepository.findById(this.virtualCashRegister.getId()).orElseThrow();
        User user = userRepository.findById(this.user.getId()).orElseThrow();

        Assertions.assertEquals(200, virtualCashRegister.getBalance().intValue());
        Assertions.assertEquals(200, user.getDebt().intValue());
    }

    @Override
    @Test
    public void deleteRequest()
    {
        super.deleteRequest();
        VirtualCashRegister virtualCashRegister = virtualCashRegisterRepository.findById(this.virtualCashRegister.getId()).orElseThrow();
        User user = userRepository.findById(this.user.getId()).orElseThrow();

        Assertions.assertEquals(0, virtualCashRegister.getBalance().intValue());
        Assertions.assertEquals(0, user.getDebt().intValue());
    }
}
