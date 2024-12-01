package de.dhbw.elinor2;

import de.dhbw.elinor2.entities.PaymentInfo;
import de.dhbw.elinor2.repositories.PaymentInfoRepository;
import de.dhbw.elinor2.utils.TestObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PaymentInfoTest extends GenericTest<PaymentInfo, UUID>
{
    @Autowired
    private PaymentInfoRepository paymentInfoRepository;

    private final String BASE_URL = "http://localhost:8080/api/paymentinfos";

    @Override
    public String getObjectAssertionIdentification(PaymentInfo paymentInfo)
    {
        return  paymentInfo.getName();
    }

    @Override
    public TestObject<PaymentInfo, UUID> initTestObject()
    {
        TestObject<PaymentInfo, UUID> testObject = new TestObject<>();
        testObject.setBaseUrl(BASE_URL);
        testObject.setRepository(paymentInfoRepository);
        testObject.setEntityClass(PaymentInfo.class);
        testObject.setEntityArrayClass(PaymentInfo[].class);

        PaymentInfo initPaymentInfo = new PaymentInfo();
        initPaymentInfo.setName("testName");
        initPaymentInfo = paymentInfoRepository.save(initPaymentInfo);
        testObject.setInitPathId(initPaymentInfo.getId());
        testObject.setInitEntity(initPaymentInfo);

        PaymentInfo updatePaymentInfo = new PaymentInfo();
        updatePaymentInfo.setName("updatedName");
        updatePaymentInfo.setId(initPaymentInfo.getId());
        testObject.setUpdateEntity(updatePaymentInfo);

        PaymentInfo newPaymentInfo = new PaymentInfo();
        newPaymentInfo.setName("newTestName");
        testObject.setNewEntity(newPaymentInfo);

        return testObject;
    }
}
