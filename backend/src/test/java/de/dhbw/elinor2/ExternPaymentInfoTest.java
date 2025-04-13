package de.dhbw.elinor2;

import de.dhbw.elinor2.entities.Extern;
import de.dhbw.elinor2.entities.Extern_PaymentInfo;
import de.dhbw.elinor2.entities.Extern_PaymentInfo_Id;
import de.dhbw.elinor2.entities.PaymentInfo;
import de.dhbw.elinor2.repositories.ExternRepository;
import de.dhbw.elinor2.repositories.Extern_PaymentInfoRepository;
import de.dhbw.elinor2.repositories.PaymentInfoRepository;
import de.dhbw.elinor2.services.ExternService;
import de.dhbw.elinor2.utils.DefaultUser;
import de.dhbw.elinor2.utils.TestSecurityConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Import(TestSecurityConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ExternPaymentInfoTest
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

    private String baseUrl;

    @BeforeEach
    void addTestData()
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

        baseUrl = "http://localhost:8080/api/externs/" + existingExtern_PaymentInfo.getExtern().getId() + "/paymentinfos";
    }

    @AfterEach
    void deleteTestData()
    {
        extern_paymentInfoRepository.deleteAll();
        externRepository.deleteAll();
        paymentInfoRepository.deleteAll();
    }

    @Test
    void getRequest_Single()
    {
        TestRestTemplate restTemplate = DefaultUser.createTestRestTemplateWithJwt();
        ResponseEntity<Extern_PaymentInfo> response =
                restTemplate.getForEntity(baseUrl + "/" + existingExtern_PaymentInfo.getPaymentInfo().getId(), Extern_PaymentInfo.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(existingExtern_PaymentInfo.getPaymentAddress(), response.getBody().getPaymentAddress());
    }

    @Test
    void getRequest_All()
    {
        TestRestTemplate restTemplate = DefaultUser.createTestRestTemplateWithJwt();
        ResponseEntity<Extern_PaymentInfo[]> response =
                restTemplate.getForEntity(baseUrl, Extern_PaymentInfo[].class);
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

        TestRestTemplate restTemplate = DefaultUser.createTestRestTemplateWithJwt();
        ResponseEntity<Extern_PaymentInfo> response =
                restTemplate.postForEntity(baseUrl + "/" + responsePaymentInfo.getId(), extern_paymentInfo.getPaymentAddress(), Extern_PaymentInfo.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(extern_paymentInfo.getPaymentAddress(), response.getBody().getPaymentAddress());
    }

    @Test
    void putRequest()
    {
        TestRestTemplate restTemplate = DefaultUser.createTestRestTemplateWithJwt();
        String newPaymentAddress = "newTestPaymentAddress";
        restTemplate.put(baseUrl + "/" + existingExtern_PaymentInfo.getPaymentInfo().getId(), newPaymentAddress);
        ResponseEntity<Extern_PaymentInfo> response = restTemplate.getForEntity(baseUrl + "/" + existingExtern_PaymentInfo.getPaymentInfo().getId(), Extern_PaymentInfo.class);

        // Verify the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(newPaymentAddress, response.getBody().getPaymentAddress());
    }

    @Test
    void deleteRequest()
    {
        TestRestTemplate restTemplate = DefaultUser.createTestRestTemplateWithJwt();
        restTemplate.delete(baseUrl + "/" + existingExtern_PaymentInfo.getPaymentInfo().getId());
        Extern_PaymentInfo_Id extern_paymentInfo_id = new Extern_PaymentInfo_Id(
                existingExtern_PaymentInfo.getExtern(),
                existingExtern_PaymentInfo.getPaymentInfo());
        Assertions.assertFalse(extern_paymentInfoRepository.existsById(extern_paymentInfo_id));
    }
}
