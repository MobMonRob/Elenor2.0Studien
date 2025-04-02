package de.dhbw.elinor2.Payments;

import de.dhbw.elinor2.entities.Extern;
import de.dhbw.elinor2.entities.ExternToUser;
import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.ExternRepository;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
import de.dhbw.elinor2.repositories.payments.ExternToUserRepository;
import de.dhbw.elinor2.services.payments.executiong.ExternToUserService;
import de.dhbw.elinor2.utils.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ExternToUserTest extends GenericTest<InputPaymentOverVcr, ExternToUser, OutputPaymentOverVcr, UUID>
{
    @Autowired
    private ExternToUserRepository externToUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VirtualCashRegisterRepository virtualCashRegisterRepository;

    @Autowired
    private ExternRepository externRepository;

    @Autowired
    private ExternToUserService externToUserService;

    private User user;

    private VirtualCashRegister virtualCashRegister;

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
    public String getObjectAssertionIdentificationReceivedEntity(InputPaymentOverVcr paymentOverVCRLight)
    {
        return paymentOverVCRLight.getSenderId().toString() +
                paymentOverVCRLight.getReceiverId().toString() +
                paymentOverVCRLight.getVcrId().toString() +
                paymentOverVCRLight.getAmount().intValue();
    }

    @Override
    public String getObjectAssertionIdentificationSendEntity(OutputPaymentOverVcr outputPaymentOverVcr)
    {
        return outputPaymentOverVcr.getSender().getEntityId().toString() +
                outputPaymentOverVcr.getReceiver().getEntityId().toString() +
                outputPaymentOverVcr.getOverVcr().getEntityId().toString() +
                outputPaymentOverVcr.getAmount().intValue();
    }

    @Override
    public TestObject<InputPaymentOverVcr, ExternToUser, OutputPaymentOverVcr, UUID> initTestObject()
    {
        TestObject<InputPaymentOverVcr, ExternToUser, OutputPaymentOverVcr, UUID> testObject = new TestObject<>();
        testObject.setEntityClass(OutputPaymentOverVcr.class);
        testObject.setEntityArrayClass(OutputPaymentOverVcr[].class);
        testObject.setRepository(externToUserRepository);
        testObject.setBaseUrl("http://localhost:8080/api/payments");

        user = DefaultUser.getDefaultUser();
        user = userRepository.save(user);

        Extern extern = new Extern();
        extern.setName("testExtern");
        extern = externRepository.save(extern);

        virtualCashRegister = new VirtualCashRegister();
        virtualCashRegister.setName("testVCR");
        virtualCashRegister = virtualCashRegisterRepository.save(virtualCashRegister);

        ExternToUser externToUser = new ExternToUser();
        externToUser.setExtern(extern);
        externToUser.setUser(user);
        externToUser.setVirtualCashRegister(virtualCashRegister);
        externToUser.setAmount(BigDecimal.valueOf(100));
        externToUserService.executePayment(externToUser, DefaultUser.getJwtToken());
        externToUser = externToUserRepository.save(externToUser);

        testObject.setInitSavedEntity(externToUser);
        testObject.setInitSavedEntityId(externToUser.getId());


        InputPaymentOverVcr updatedPayment = new InputPaymentOverVcr();
        updatedPayment.setSenderId(extern.getId());
        updatedPayment.setReceiverId(user.getId());
        updatedPayment.setVcrId(virtualCashRegister.getId());
        updatedPayment.setAmount(BigDecimal.valueOf(200));
        testObject.setUpdateEntity(updatedPayment);

        InputPaymentOverVcr newPayment = new InputPaymentOverVcr();
        newPayment.setSenderId(extern.getId());
        newPayment.setReceiverId(user.getId());
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
