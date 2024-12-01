package de.dhbw.elinor2;

import de.dhbw.elinor2.entities.Extern;
import de.dhbw.elinor2.entities.Extern_PaymentInfo;
import de.dhbw.elinor2.entities.Extern_PaymentInfo_Id;
import de.dhbw.elinor2.entities.PaymentInfo;
import de.dhbw.elinor2.repositories.ExternRepository;
import de.dhbw.elinor2.repositories.Extern_PaymentInfoRepository;
import de.dhbw.elinor2.repositories.PaymentInfoRepository;
import de.dhbw.elinor2.services.ExternService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ExternPaymentInfoTest //extends GenericTest<Extern_PaymentInfo, Extern_PaymentInfo_Id>
{
    @Autowired
    private ExternService externService;

    @Autowired
    private Extern_PaymentInfoRepository extern_paymentInfoRepository;

    @Autowired
    private ExternRepository externRepository;

    @Autowired
    private PaymentInfoRepository paymentInfoRepository;

    private Extern_PaymentInfo existingExtern_PaymentInfo;

    private String BASE_URL;

    @BeforeEach
    public void addTestData()
    {
        Extern extern = new Extern();
        extern.setName("testName");
        Extern responseExtern = externRepository.save(extern);

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setName("testName");
        PaymentInfo responsePaymentInfo = paymentInfoRepository.save(paymentInfo);

        existingExtern_PaymentInfo = externService.createExternPaymentInfo(
                responseExtern.getId(),
                responsePaymentInfo.getId(),
                "testPaymentAddress");

        BASE_URL = "http://localhost:8080/api/externs/" + existingExtern_PaymentInfo.getExtern().getId() + "/paymentinfos";
    }

    @AfterEach
    public void deleteTestData()
    {
        extern_paymentInfoRepository.deleteAll();
        externRepository.deleteAll();
        paymentInfoRepository.deleteAll();
    }

    @Test
    void getRequest_Single()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<Extern_PaymentInfo> response =
                restTemplate.getForEntity(BASE_URL + "/" + existingExtern_PaymentInfo.getPaymentInfo().getId(), Extern_PaymentInfo.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(existingExtern_PaymentInfo.getPaymentAddress(), response.getBody().getPaymentAddress());
    }

    @Test
    void getRequest_All()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<Extern_PaymentInfo[]> response =
                restTemplate.getForEntity(BASE_URL, Extern_PaymentInfo[].class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().length);
        Assertions.assertEquals(existingExtern_PaymentInfo.getPaymentAddress(), response.getBody()[0].getPaymentAddress());
    }

    @Test
    void postRequest()
    {
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setName("newTestName");
        PaymentInfo responsePaymentInfo = paymentInfoRepository.save(paymentInfo);

        Extern_PaymentInfo extern_paymentInfo = new Extern_PaymentInfo();
        extern_paymentInfo.setExtern(existingExtern_PaymentInfo.getExtern());
        extern_paymentInfo.setPaymentInfo(responsePaymentInfo);
        extern_paymentInfo.setPaymentAddress("newTestPaymentAddress");

        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<Extern_PaymentInfo> response =
                restTemplate.postForEntity(BASE_URL + "/" + responsePaymentInfo.getId(), extern_paymentInfo.getPaymentAddress(), Extern_PaymentInfo.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(extern_paymentInfo.getPaymentAddress(), response.getBody().getPaymentAddress());
    }

    @Test
    void putRequest()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String newPaymentAddress = "newTestPaymentAddress";
        restTemplate.put(BASE_URL + "/" + existingExtern_PaymentInfo.getPaymentInfo().getId(), newPaymentAddress);
        ResponseEntity<Extern_PaymentInfo> response = restTemplate.getForEntity(BASE_URL + "/" + existingExtern_PaymentInfo.getPaymentInfo().getId(), Extern_PaymentInfo.class);

        // Verify the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(newPaymentAddress, response.getBody().getPaymentAddress());
    }

    @Test
    void deleteRequest()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        restTemplate.delete(BASE_URL + "/" + existingExtern_PaymentInfo.getPaymentInfo().getId());
        Extern_PaymentInfo_Id extern_paymentInfo_id = new Extern_PaymentInfo_Id(
                existingExtern_PaymentInfo.getExtern(),
                existingExtern_PaymentInfo.getPaymentInfo());
        Assertions.assertFalse(extern_paymentInfoRepository.existsById(extern_paymentInfo_id));
    }

    /*
    @Override
    @AfterEach
    public void deleteTestData()
    {
        extern_paymentInfoRepository.deleteAll();
        externRepository.deleteAll();
        paymentInfoRepository.deleteAll();
    }

    @Override
    public String getObjectAssertionIdentification(Extern_PaymentInfo externPaymentInfo)
    {
        return externPaymentInfo.getPaymentAddress() +
                externPaymentInfo.getExtern().getName() +
                externPaymentInfo.getPaymentInfo().getName();
    }

    @Override
    public TestObject<Extern_PaymentInfo, Extern_PaymentInfo_Id> initTestObject()
    {
        Extern extern = new Extern();
        extern.setName("testName");
        Extern responseExtern = externRepository.save(extern);

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setName("testName");
        PaymentInfo responsePaymentInfo = paymentInfoRepository.save(paymentInfo);

        BASE_URL = "http://localhost:8080/api/externs/" + extern.getId() + "/paymentinfos";

        TestObject<Extern_PaymentInfo, Extern_PaymentInfo_Id> testObject = new TestObject<>();
        testObject.setRepository(extern_paymentInfoRepository);
        testObject.setBaseUrl(BASE_URL);
        testObject.setEntityClass(Extern_PaymentInfo.class);
        testObject.setEntityArrayClass(Extern_PaymentInfo[].class);

        Extern_PaymentInfo extern_paymentInfo = externService.createExternPaymentInfo(
                responseExtern.getId(),
                responsePaymentInfo.getId(),
                "testPaymentAddress");
        testObject.setInitEntity(extern_paymentInfo);

        Extern_PaymentInfo_Id extern_paymentInfo_id = new Extern_PaymentInfo_Id(
                extern_paymentInfo.getExtern(),
                extern_paymentInfo.getPaymentInfo());
        testObject.setInitPathId(paymentInfo.getId());

        Extern_PaymentInfo updateExtern_PaymentInfo = new Extern_PaymentInfo();
        updateExtern_PaymentInfo.setExtern(extern);
        updateExtern_PaymentInfo.setPaymentInfo(paymentInfo);
        updateExtern_PaymentInfo.setPaymentAddress("updatedPaymentAddress");
        testObject.setUpdateEntity(updateExtern_PaymentInfo);

        PaymentInfo newPaymentInfo = new PaymentInfo();
        newPaymentInfo.setName("newTestName");
        paymentInfoRepository.save(newPaymentInfo);

        Extern_PaymentInfo newExtern_PaymentInfo = new Extern_PaymentInfo();
        newExtern_PaymentInfo.setExtern(extern);
        newExtern_PaymentInfo.setPaymentInfo(newPaymentInfo);
        newExtern_PaymentInfo.setPaymentAddress("newPaymentAddress");
        testObject.setNewEntity(newExtern_PaymentInfo);

        return testObject;
    }*/
}
