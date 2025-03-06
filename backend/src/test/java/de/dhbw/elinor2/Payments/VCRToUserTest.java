package de.dhbw.elinor2.Payments;

import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.VCRToUser;
import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import de.dhbw.elinor2.repositories.payments.VCRToUserRepository;
import de.dhbw.elinor2.services.payments.documenting.VCRToUserService;
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
public class VCRToUserTest extends GenericTest<PaymentLight, VCRToUser, UUID>
{
    @Autowired
    private VCRToUserRepository vcrToUserRepository;

    @Autowired
    private VCRToUserService vcrToUserService;

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
        vcrToUserRepository.deleteAll();
        userRepository.deleteAll();
        virtualCashRegisterRepository.deleteAll();
    }

    @Override
    public String getObjectAssertionIdentificationSavedEntity(VCRToUser vcrToUser)
    {
        return vcrToUser.getVirtualCashRegister().getId().toString() +
                vcrToUser.getUser().getId().toString() +
                vcrToUser.getAmount().intValue();
    }

    @Override
    public String getObjectAssertionIdentificationReceivedEntity(PaymentLight paymentLight)
    {
        return paymentLight.getSenderId().toString() +
                paymentLight.getReceiverId().toString() +
                paymentLight.getAmount().intValue();
    }

    @Override
    public TestObject<PaymentLight, VCRToUser, UUID> initTestObject()
    {
        TestObject<PaymentLight, VCRToUser, UUID> testObject = new TestObject<>();
        testObject.setEntityClass(VCRToUser.class);
        testObject.setEntityArrayClass(VCRToUser[].class);
        testObject.setRepository(vcrToUserRepository);
        testObject.setBaseUrl("http://localhost:8080/api/payments/doc/vcrtousers");

        user = DefaultUser.getDefaultUser();
        user = userRepository.save(user);

        virtualCashRegister = new VirtualCashRegister();
        virtualCashRegister.setName("testVCR");
        virtualCashRegister = virtualCashRegisterRepository.save(virtualCashRegister);

        PaymentLight paymentLight = new PaymentLight();
        paymentLight.setSenderId(virtualCashRegister.getId());
        paymentLight.setReceiverId(user.getId());
        paymentLight.setAmount(BigDecimal.valueOf(100));
        VCRToUser vcrToUser = vcrToUserService.create(paymentLight, DefaultUser.getJwtToken());
        testObject.setInitSavedEntity(vcrToUser);
        testObject.setInitSavedEntityId(vcrToUser.getId());

        PaymentLight updatedPaymentLight = new PaymentLight();
        updatedPaymentLight.setSenderId(virtualCashRegister.getId());
        updatedPaymentLight.setReceiverId(user.getId());
        updatedPaymentLight.setAmount(BigDecimal.valueOf(200));
        testObject.setUpdateEntity(updatedPaymentLight);

        PaymentLight newPaymentLight = new PaymentLight();
        newPaymentLight.setSenderId(virtualCashRegister.getId());
        newPaymentLight.setReceiverId(user.getId());
        newPaymentLight.setAmount(BigDecimal.valueOf(300));
        testObject.setNewEntity(newPaymentLight);

        return testObject;
    }

    @Override
    @Test
    public void postRequest()
    {
        super.postRequest();
        VirtualCashRegister virtualCashRegisterResult = virtualCashRegisterRepository.findById(this.virtualCashRegister.getId()).orElseThrow();
        User userResult = userRepository.findById(this.user.getId()).orElseThrow();

        Assertions.assertEquals(-400, virtualCashRegisterResult.getBalance().intValue());
        Assertions.assertEquals(-400, userResult.getDebt().intValue());
    }

    @Override
    @Test
    public void putRequest()
    {
        super.putRequest();
        VirtualCashRegister virtualCashRegisterResult = virtualCashRegisterRepository.findById(this.virtualCashRegister.getId()).orElseThrow();
        User userResult = userRepository.findById(this.user.getId()).orElseThrow();

        Assertions.assertEquals(-200, virtualCashRegisterResult.getBalance().intValue());
        Assertions.assertEquals(-200, userResult.getDebt().intValue());
    }

    @Override
    @Test
    public void deleteRequest()
    {
        super.deleteRequest();
        VirtualCashRegister virtualCashRegisterResult = virtualCashRegisterRepository.findById(this.virtualCashRegister.getId()).orElseThrow();
        User userResult = userRepository.findById(this.user.getId()).orElseThrow();

        Assertions.assertEquals(0, virtualCashRegisterResult.getBalance().intValue());
        Assertions.assertEquals(0, userResult.getDebt().intValue());
    }
}
