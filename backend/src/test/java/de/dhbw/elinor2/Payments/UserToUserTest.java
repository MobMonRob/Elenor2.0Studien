package de.dhbw.elinor2.Payments;

import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.UserToUser;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.payments.UserToUserRepository;
import de.dhbw.elinor2.services.payments.executiong.UserToUserService;
import de.dhbw.elinor2.utils.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserToUserTest extends GenericTest<InputPayment, UserToUser, OutputPaymentOverVcr, UUID>
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
    public TestObject<InputPayment, UserToUser, OutputPaymentOverVcr, UUID> initTestObject()
    {
        TestObject<InputPayment, UserToUser, OutputPaymentOverVcr, UUID> testObject = new TestObject<>();
        testObject.setEntityClass(OutputPaymentOverVcr.class);
        testObject.setEntityArrayClass(OutputPaymentOverVcr[].class);
        testObject.setRepository(userToUserRepository);
        testObject.setBaseUrl("http://localhost:8080/api/payments");

        sender = DefaultUser.getDefaultUser();
        sender = userRepository.save(sender);

        receiver = new User();
        receiver.setId(UUID.randomUUID());
        receiver.setUsername("testReceiver");
        receiver.setFirstName("testFirstname");
        receiver.setLastName("testLastname");
        receiver = userRepository.save(receiver);

        UserToUser userToUser = new UserToUser();
        userToUser.setSender(sender);
        userToUser.setReceiver(receiver);
        userToUser.setAmount(BigDecimal.valueOf(100));
        userToUserService.executePayment(userToUser, DefaultUser.getJwtToken());
        userToUser = userToUserRepository.save(userToUser);

        testObject.setInitSavedEntity(userToUser);
        testObject.setInitSavedEntityId(userToUser.getId());

        InputPayment updated = new InputPayment();
        updated.setSenderId(sender.getId());
        updated.setReceiverId(receiver.getId());
        updated.setAmount(BigDecimal.valueOf(200));
        testObject.setUpdateEntity(updated);

        InputPayment newPayment = new InputPayment();
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
        User senderResult = userRepository.findById(this.sender.getId()).orElseThrow();
        User receiverResult = userRepository.findById(this.receiver.getId()).orElseThrow();

        Assertions.assertEquals(400, senderResult.getBalance().intValue());
        Assertions.assertEquals(-400, receiverResult.getBalance().intValue());
    }

    @Override
    @Test
    public void putRequest()
    {
        super.putRequest();
        User senderResult = userRepository.findById(this.sender.getId()).orElseThrow();
        User receiverResult = userRepository.findById(this.receiver.getId()).orElseThrow();

        Assertions.assertEquals(200, senderResult.getBalance().intValue());
        Assertions.assertEquals(-200, receiverResult.getBalance().intValue());
    }

    @Override
    @Test
    public void deleteRequest()
    {
        super.deleteRequest();
        User senderResult = userRepository.findById(this.sender.getId()).orElseThrow();
        User receiverResult = userRepository.findById(this.receiver.getId()).orElseThrow();

        Assertions.assertEquals(0, senderResult.getBalance().intValue());
        Assertions.assertEquals(0, receiverResult.getBalance().intValue());
    }
}
