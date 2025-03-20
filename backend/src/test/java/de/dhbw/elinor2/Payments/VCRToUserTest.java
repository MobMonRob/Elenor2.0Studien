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
import de.dhbw.elinor2.utils.InputPayment;
import de.dhbw.elinor2.utils.TestObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class VCRToUserTest extends GenericTest<InputPayment, VCRToUser, UUID>
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
    public String getObjectAssertionIdentificationReceivedEntity(InputPayment inputPaymentLight)
    {
        return inputPaymentLight.getSenderId().toString() +
                inputPaymentLight.getReceiverId().toString() +
                inputPaymentLight.getAmount().intValue();
    }

    @Override
    public TestObject<InputPayment, VCRToUser, UUID> initTestObject()
    {
        TestObject<InputPayment, VCRToUser, UUID> testObject = new TestObject<>();
        testObject.setEntityClass(VCRToUser.class);
        testObject.setEntityArrayClass(VCRToUser[].class);
        testObject.setRepository(vcrToUserRepository);
        testObject.setBaseUrl("http://localhost:8080/api/payments/doc/vcrtousers");

        user = DefaultUser.getDefaultUser();
        user = userRepository.save(user);

        virtualCashRegister = new VirtualCashRegister();
        virtualCashRegister.setName("testVCR");
        virtualCashRegister = virtualCashRegisterRepository.save(virtualCashRegister);

        InputPayment inputPaymentLight = new InputPayment();
        inputPaymentLight.setSenderId(virtualCashRegister.getId());
        inputPaymentLight.setReceiverId(user.getId());
        inputPaymentLight.setAmount(BigDecimal.valueOf(100));
        VCRToUser vcrToUser = vcrToUserService.create(inputPaymentLight, DefaultUser.getJwtToken());
        testObject.setInitSavedEntity(vcrToUser);
        testObject.setInitSavedEntityId(vcrToUser.getId());

        InputPayment updatedInputPaymentLight = new InputPayment();
        updatedInputPaymentLight.setSenderId(virtualCashRegister.getId());
        updatedInputPaymentLight.setReceiverId(user.getId());
        updatedInputPaymentLight.setAmount(BigDecimal.valueOf(200));
        testObject.setUpdateEntity(updatedInputPaymentLight);

        InputPayment newInputPaymentLight = new InputPayment();
        newInputPaymentLight.setSenderId(virtualCashRegister.getId());
        newInputPaymentLight.setReceiverId(user.getId());
        newInputPaymentLight.setAmount(BigDecimal.valueOf(300));
        testObject.setNewEntity(newInputPaymentLight);

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
