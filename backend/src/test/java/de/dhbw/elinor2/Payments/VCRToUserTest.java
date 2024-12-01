package de.dhbw.elinor2.Payments;

import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.VCRToUser;
import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import de.dhbw.elinor2.repositories.payments.VCRToUserRepository;
import de.dhbw.elinor2.utils.GenericTest;
import de.dhbw.elinor2.utils.PaymentLight;
import de.dhbw.elinor2.utils.TestObject;
import org.junit.jupiter.api.AfterEach;
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
    private UserRepository userRepository;

    @Autowired
    private VirtualCashRegisterRepository virtualCashRegisterRepository;

    private String BASE_URL = "http://localhost:8080/api/payments/doc/vcrtousers";

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
        testObject.setBaseUrl(BASE_URL);

        User user = new User();
        user.setUsername("testUsername");
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");
        user = userRepository.save(user);

        VirtualCashRegister virtualCashRegister = new VirtualCashRegister();
        virtualCashRegister.setName("testVCR");
        virtualCashRegister = virtualCashRegisterRepository.save(virtualCashRegister);

        VCRToUser vcrToUser = new VCRToUser();
        vcrToUser.setUser(user);
        vcrToUser.setVirtualCashRegister(virtualCashRegister);
        vcrToUser.setAmount(BigDecimal.valueOf(100));
        vcrToUser = vcrToUserRepository.save(vcrToUser);
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
}
