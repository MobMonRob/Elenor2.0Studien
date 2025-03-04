package de.dhbw.elinor2.Payments;

import de.dhbw.elinor2.entities.Extern;
import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.UserToExtern;
import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.ExternRepository;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import de.dhbw.elinor2.repositories.payments.UserToExternRepository;
import de.dhbw.elinor2.services.payments.executiong.UserToExternService;
import de.dhbw.elinor2.utils.DefaultUser;
import de.dhbw.elinor2.utils.GenericTest;
import de.dhbw.elinor2.utils.PaymentOverVCRLight;
import de.dhbw.elinor2.utils.TestObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
    private UserToExternService userToExternService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VirtualCashRegisterRepository virtualCashRegisterRepository;

    @Autowired
    private ExternRepository externRepository;

    private User user;

    private VirtualCashRegister virtualCashRegister;

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
        testObject.setBaseUrl("http://localhost:8080/api/payments/exec/usertoexterns");

        user = DefaultUser.getDefaultUser();
        user = userRepository.save(user);

        Extern extern = new Extern();
        extern.setName("testExtern");
        extern = externRepository.save(extern);

        virtualCashRegister = new VirtualCashRegister();
        virtualCashRegister.setName("testVCR");
        virtualCashRegister = virtualCashRegisterRepository.save(virtualCashRegister);

        PaymentOverVCRLight initialPayment = new PaymentOverVCRLight();
        initialPayment.setSenderId(user.getId());
        initialPayment.setReceiverId(extern.getId());
        initialPayment.setVcrId(virtualCashRegister.getId());
        initialPayment.setAmount(BigDecimal.valueOf(100));
        UserToExtern userToExtern = userToExternService.create(initialPayment, DefaultUser.getJwtToken());
        testObject.setInitSavedEntity(userToExtern);
        testObject.setInitSavedEntityId(userToExtern.getId());


        PaymentOverVCRLight updatedPayment = new PaymentOverVCRLight();
        updatedPayment.setSenderId(user.getId());
        updatedPayment.setReceiverId(extern.getId());
        updatedPayment.setVcrId(virtualCashRegister.getId());
        updatedPayment.setAmount(BigDecimal.valueOf(200));
        testObject.setUpdateEntity(updatedPayment);

        PaymentOverVCRLight newPayment = new PaymentOverVCRLight();
        newPayment.setSenderId(user.getId());
        newPayment.setReceiverId(extern.getId());
        newPayment.setVcrId(virtualCashRegister.getId());
        newPayment.setAmount(BigDecimal.valueOf(300));
        testObject.setNewEntity(newPayment);

        return testObject;
    }

    @Override
    @Test
    public void postRequest()
    {
        super.postRequest();
        VirtualCashRegister virtualCashRegister = virtualCashRegisterRepository.findById(this.virtualCashRegister.getId()).orElseThrow();
        User user = userRepository.findById(this.user.getId()).orElseThrow();

        Assertions.assertEquals(-400, virtualCashRegister.getBalance().intValue());
        Assertions.assertEquals(-400, user.getDebt().intValue());
    }

    @Override
    @Test
    public void putRequest()
    {
        super.putRequest();
        VirtualCashRegister virtualCashRegister = virtualCashRegisterRepository.findById(this.virtualCashRegister.getId()).orElseThrow();
        User user = userRepository.findById(this.user.getId()).orElseThrow();

        Assertions.assertEquals(-200, virtualCashRegister.getBalance().intValue());
        Assertions.assertEquals(-200, user.getDebt().intValue());
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
