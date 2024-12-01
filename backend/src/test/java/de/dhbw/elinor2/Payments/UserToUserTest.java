package de.dhbw.elinor2.Payments;

import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.UserToUser;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.payments.UserToUserRepository;
import de.dhbw.elinor2.utils.GenericTest;
import de.dhbw.elinor2.utils.PaymentLight;
import de.dhbw.elinor2.utils.TestObject;
import org.junit.jupiter.api.AfterEach;
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
    private UserRepository userRepository;

    private String BASE_URL = "http://localhost:8080/api/payments/exec/usertousers";

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
        testObject.setBaseUrl(BASE_URL);

        User sender = new User();
        sender.setUsername("testSender");
        sender.setFirstName("testFirstname");
        sender.setLastName("testLastname");
        sender = userRepository.save(sender);

        User receiver = new User();
        receiver.setUsername("testReceiver");
        receiver.setFirstName("testFirstname");
        receiver.setLastName("testLastname");
        receiver = userRepository.save(receiver);

        UserToUser userToUser = new UserToUser();
        userToUser.setSender(sender);
        userToUser.setReceiver(receiver);
        userToUser.setAmount(BigDecimal.valueOf(50));
        userToUser = userToUserRepository.save(userToUser);
        testObject.setInitSavedEntity(userToUser);
        testObject.setInitSavedEntityId(userToUser.getId());

        PaymentLight updated = new PaymentLight();
        updated.setSenderId(sender.getId());
        updated.setReceiverId(receiver.getId());
        updated.setAmount(BigDecimal.valueOf(100));
        testObject.setUpdateEntity(updated);

        PaymentLight newPayment = new PaymentLight();
        newPayment.setSenderId(sender.getId());
        newPayment.setReceiverId(receiver.getId());
        newPayment.setAmount(BigDecimal.valueOf(200));
        testObject.setNewEntity(newPayment);

        return testObject;
    }
}
