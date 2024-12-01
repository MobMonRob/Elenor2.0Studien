package de.dhbw.elinor2.Payments;

import de.dhbw.elinor2.entities.Extern;
import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.UserToExtern;
import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.ExternRepository;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import de.dhbw.elinor2.repositories.payments.UserToExternRepository;
import de.dhbw.elinor2.utils.GenericTest;
import de.dhbw.elinor2.utils.PaymentOverVCRLight;
import de.dhbw.elinor2.utils.TestObject;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserToExternTest extends GenericTest<PaymentOverVCRLight, UserToExtern, UUID>
{
    @Autowired
    private UserToExternRepository userToExternRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VirtualCashRegisterRepository virtualCashRegisterRepository;

    @Autowired
    private ExternRepository externRepository;

    private String BASE_URL = "http://localhost:8080/api/payments/exec/usertoexterns";

    @Override
    @AfterEach
    public void deleteTestData()
    {
        userToExternRepository.deleteAll();
        userRepository.deleteAll();
        virtualCashRegisterRepository.deleteAll();
        externRepository.deleteAll();
    }

    @Override
    public String getObjectAssertionIdentificationSavedEntity(UserToExtern userToExtern)
    {
        return userToExtern.getUser().getId().toString() +
                userToExtern.getExtern().getId().toString() +
                userToExtern.getVirtualCashRegister().getId().toString() +
                userToExtern.getAmount().intValue();
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
    public TestObject<PaymentOverVCRLight, UserToExtern, UUID> initTestObject()
    {
        TestObject<PaymentOverVCRLight, UserToExtern, UUID> testObject = new TestObject<>();
        testObject.setEntityClass(UserToExtern.class);
        testObject.setEntityArrayClass(UserToExtern[].class);
        testObject.setRepository(userToExternRepository);
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

        UserToExtern userToExtern = new UserToExtern();
        userToExtern.setUser(user);
        userToExtern.setExtern(extern);
        userToExtern.setVirtualCashRegister(virtualCash);
        userToExtern.setAmount(BigDecimal.valueOf(100));
        userToExtern = userToExternRepository.save(userToExtern);
        testObject.setInitSavedEntity(userToExtern);
        testObject.setInitSavedEntityId(userToExtern.getId());

        PaymentOverVCRLight updatedPayment = new PaymentOverVCRLight();
        updatedPayment.setSenderId(user.getId());
        updatedPayment.setReceiverId(extern.getId());
        updatedPayment.setVcrId(virtualCash.getId());
        updatedPayment.setAmount(BigDecimal.valueOf(200));
        testObject.setUpdateEntity(updatedPayment);

        PaymentOverVCRLight newPayment = new PaymentOverVCRLight();
        newPayment.setSenderId(user.getId());
        newPayment.setReceiverId(extern.getId());
        newPayment.setVcrId(virtualCash.getId());
        newPayment.setAmount(BigDecimal.valueOf(300));
        testObject.setNewEntity(newPayment);

        return testObject;
    }
}
