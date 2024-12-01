package de.dhbw.elinor2.Payments;

import de.dhbw.elinor2.entities.Extern;
import de.dhbw.elinor2.entities.ExternToUser;
import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.ExternRepository;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import de.dhbw.elinor2.repositories.payments.ExternToUserRepository;
import de.dhbw.elinor2.utils.GenericTest;
import de.dhbw.elinor2.utils.PaymentOverVCRLight;
import de.dhbw.elinor2.utils.TestObject;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ExternToUserTest extends GenericTest<PaymentOverVCRLight, ExternToUser, UUID>
{
    @Autowired
    private ExternToUserRepository externToUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VirtualCashRegisterRepository virtualCashRegisterRepository;

    @Autowired
    private ExternRepository externRepository;

    private String BASE_URL = "http://localhost:8080/api/payments/exec/externtousers";

    @Override
    @AfterEach
    public void deleteTestData()
    {
        externToUserRepository.deleteAll();
        userRepository.deleteAll();
        virtualCashRegisterRepository.deleteAll();
        externRepository.deleteAll();
    }

    @Override
    public String getObjectAssertionIdentificationSavedEntity(ExternToUser externToUser)
    {
        return externToUser.getExtern().getId().toString() +
                externToUser.getUser().getId().toString() +
                externToUser.getVirtualCashRegister().getId().toString() +
                externToUser.getAmount().intValue();
    }

    @Override
    public String getObjectAssertionIdentificationReceivedEntity(PaymentOverVCRLight paymentOverVCRLight)
    {
        return paymentOverVCRLight.getSenderId().toString() +
                paymentOverVCRLight.getReceiverId().toString() +
                paymentOverVCRLight.getVcrId().toString() +
                paymentOverVCRLight.getAmount().intValue();
    }

    @Override
    public TestObject<PaymentOverVCRLight, ExternToUser, UUID> initTestObject()
    {
        TestObject<PaymentOverVCRLight, ExternToUser, UUID> testObject = new TestObject<>();
        testObject.setEntityClass(ExternToUser.class);
        testObject.setEntityArrayClass(ExternToUser[].class);
        testObject.setRepository(externToUserRepository);
        testObject.setBaseUrl(BASE_URL);

        User user = new User();
        user.setUsername("testUsername");
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");
        user = userRepository.save(user);

        Extern extern = new Extern();
        extern.setName("testExtern");
        extern = externRepository.save(extern);

        VirtualCashRegister virtualCash = new VirtualCashRegister();
        virtualCash.setName("testVCR");
        virtualCash = virtualCashRegisterRepository.save(virtualCash);

        ExternToUser externToUser = new ExternToUser();
        externToUser.setUser(user);
        externToUser.setExtern(extern);
        externToUser.setVirtualCashRegister(virtualCash);
        externToUser.setAmount(BigDecimal.valueOf(100));
        externToUser = externToUserRepository.save(externToUser);
        testObject.setInitSavedEntity(externToUser);
        testObject.setInitSavedEntityId(externToUser.getId());

        PaymentOverVCRLight updatedPayment = new PaymentOverVCRLight();
        updatedPayment.setSenderId(extern.getId());
        updatedPayment.setReceiverId(user.getId());
        updatedPayment.setVcrId(virtualCash.getId());
        updatedPayment.setAmount(BigDecimal.valueOf(200));
        testObject.setUpdateEntity(updatedPayment);

        PaymentOverVCRLight newPayment = new PaymentOverVCRLight();
        newPayment.setSenderId(extern.getId());
        newPayment.setReceiverId(user.getId());
        newPayment.setVcrId(virtualCash.getId());
        newPayment.setAmount(BigDecimal.valueOf(300));
        testObject.setNewEntity(newPayment);

        return testObject;
    }
}
