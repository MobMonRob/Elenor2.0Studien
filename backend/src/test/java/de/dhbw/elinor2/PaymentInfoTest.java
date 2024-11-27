package de.dhbw.elinor2;
import de.dhbw.elinor2.entities.PaymentInfo;
import de.dhbw.elinor2.repositories.PaymentInfoRepository;
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
public class PaymentInfoTest
{
    @Autowired
    private PaymentInfoRepository paymentInfoRepository;

    private PaymentInfo existingPaymentInfo;

    @BeforeEach
    public void addTestData()
    {
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setName("testName");
        existingPaymentInfo = paymentInfoRepository.save(paymentInfo);
    }

    @AfterEach
    public void deleteTestData()
    {
        paymentInfoRepository.deleteAll();
    }

    @Test
    void getRequest_Single()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<PaymentInfo> response =
            restTemplate.getForEntity("http://localhost:8080/paymentinfos/" + existingPaymentInfo.getId(), PaymentInfo.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(existingPaymentInfo.getName(), response.getBody().getName());
    }

    @Test
    void getRequest_All()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<PaymentInfo[]> response =
            restTemplate.getForEntity("http://localhost:8080/paymentinfos", PaymentInfo[].class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().length);
        Assertions.assertEquals(existingPaymentInfo.getName(), response.getBody()[0].getName());
    }

    @Test
    void postRequest()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setName("newTestName");
        ResponseEntity<PaymentInfo> response =
            restTemplate.postForEntity("http://localhost:8080/paymentinfos", paymentInfo, PaymentInfo.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(paymentInfo.getName(), response.getBody().getName());
    }

    @Test
    void putRequest()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        existingPaymentInfo.setName("newTestName");
        restTemplate.put("http://localhost:8080/paymentinfos/" + existingPaymentInfo.getId(), existingPaymentInfo);
        PaymentInfo updatedPaymentInfo = paymentInfoRepository.findById(existingPaymentInfo.getId()).get();
        Assertions.assertEquals(existingPaymentInfo.getName(), updatedPaymentInfo.getName());
    }

    @Test
    void deleteRequest()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        restTemplate.delete("http://localhost:8080/paymentinfos/" + existingPaymentInfo.getId());
        Assertions.assertFalse(paymentInfoRepository.existsById(existingPaymentInfo.getId()));
    }
}
