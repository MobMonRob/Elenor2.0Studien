package de.dhbw.elinor2.Payments;

import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.UserToUser;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.payments.UserToUserRepository;
import de.dhbw.elinor2.services.payments.executiong.UserToUserService;
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
public class UserToUserTest extends GenericTest<PaymentLight, UserToUser, UUID>
{
    @Autowired
    private UserToUserRepository userToUserRepository;

    @Autowired
    private UserToUserService userToUserService;

    @Autowired
    private UserRepository userRepository;

    private User sender;
    private User receiver;

    @Override
    @AfterEach
    public void deleteTestData()
    {
        userToUserRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Override
    public String getObjectAssertionIdentificationSavedEntity(UserToUser userToUser)
    {
        return userToUser.getSender().getId().toString() +
                userToUser.getReceiver().getId().toString() +
                userToUser.getAmount().intValue();
    }

    @Override
    public String getObjectAssertionIdentificationReceivedEntity(PaymentLight paymentLight)
    {
        return paymentLight.getSenderId().toString() +
                paymentLight.getReceiverId().toString() +
                paymentLight.getAmount().intValue();
    }


    @Override
    public TestObject<PaymentLight, UserToUser, UUID> initTestObject()
    {
        TestObject<PaymentLight, UserToUser, UUID> testObject = new TestObject<>();
        testObject.setEntityClass(UserToUser.class);
        testObject.setEntityArrayClass(UserToUser[].class);
        testObject.setRepository(userToUserRepository);
        testObject.setBaseUrl("http://localhost:8080/api/payments/exec/usertousers");

        sender = DefaultUser.getDefaultUser();
        sender = userRepository.save(sender);

        receiver = new User();
        receiver.setId(UUID.randomUUID());
        receiver.setUsername("testReceiver");
        receiver.setFirstName("testFirstname");
        receiver.setLastName("testLastname");
        receiver = userRepository.save(receiver);

        PaymentLight initialPayment = new PaymentLight();
        initialPayment.setSenderId(sender.getId());
        initialPayment.setReceiverId(receiver.getId());
        initialPayment.setAmount(BigDecimal.valueOf(100));
        UserToUser userToUser = userToUserService.create(initialPayment, DefaultUser.getJwtToken());
        testObject.setInitSavedEntity(userToUser);
        testObject.setInitSavedEntityId(userToUser.getId());

        PaymentLight updated = new PaymentLight();
        updated.setSenderId(sender.getId());
        updated.setReceiverId(receiver.getId());
        updated.setAmount(BigDecimal.valueOf(200));
        testObject.setUpdateEntity(updated);

        PaymentLight newPayment = new PaymentLight();
        newPayment.setSenderId(sender.getId());
        newPayment.setReceiverId(receiver.getId());
        newPayment.setAmount(BigDecimal.valueOf(300));
        testObject.setNewEntity(newPayment);

        return testObject;
    }

    @Override
    @Test
    public void postRequest()
    {
        super.postRequest();
        User sender = userRepository.findById(this.sender.getId()).orElseThrow();
        User receiver = userRepository.findById(this.receiver.getId()).orElseThrow();

        Assertions.assertEquals(-400, sender.getDebt().intValue());
        Assertions.assertEquals(400, receiver.getDebt().intValue());
    }

    @Override
    @Test
    public void putRequest()
    {
        super.putRequest();
        User sender = userRepository.findById(this.sender.getId()).orElseThrow();
        User receiver = userRepository.findById(this.receiver.getId()).orElseThrow();

        Assertions.assertEquals(-200, sender.getDebt().intValue());
        Assertions.assertEquals(200, receiver.getDebt().intValue());
    }

    @Override
    @Test
    public void deleteRequest()
    {
        super.deleteRequest();
        User sender = userRepository.findById(this.sender.getId()).orElseThrow();
        User receiver = userRepository.findById(this.receiver.getId()).orElseThrow();

        Assertions.assertEquals(0, sender.getDebt().intValue());
        Assertions.assertEquals(0, receiver.getDebt().intValue());
    }
}
