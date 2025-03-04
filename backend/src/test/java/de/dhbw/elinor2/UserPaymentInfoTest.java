package de.dhbw.elinor2;

import de.dhbw.elinor2.entities.PaymentInfo;
import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.User_PaymentInfo;
import de.dhbw.elinor2.entities.User_PaymentInfo_Id;
import de.dhbw.elinor2.repositories.PaymentInfoRepository;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.User_PaymentInfoRepository;
import de.dhbw.elinor2.services.UserService;
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
public class UserPaymentInfoTest
{
    @Autowired
    private UserService userService;

    @Autowired
    private User_PaymentInfoRepository user_paymentInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentInfoRepository paymentInfoRepository;

    private User_PaymentInfo existingUser_PaymentInfo;

    private String BASE_URL;

    @BeforeEach
    public void addTestData()
    {
        User user = DefaultUser.getDefaultUser();
        User responseUser = userRepository.save(user);

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setName("testName");
        PaymentInfo responsePaymentInfo = paymentInfoRepository.save(paymentInfo);

        existingUser_PaymentInfo = userService.createUserPaymentInfo(
                responseUser.getId(),
                responsePaymentInfo.getId(),
                "testPaymentAddress");

        BASE_URL = "http://localhost:8080/api/users/" + existingUser_PaymentInfo.getUser().getId() + "/paymentinfos";
    }

    @AfterEach
    public void deleteTestData()
    {
        user_paymentInfoRepository.deleteAll();
        userRepository.deleteAll();
        paymentInfoRepository.deleteAll();
    }

    @Test
    void getRequest_Single()
    {
        TestRestTemplate restTemplate = DefaultUser.createTestRestTemplateWithJwt();
        ResponseEntity<User_PaymentInfo> response =
                restTemplate.getForEntity(BASE_URL + "/" + existingUser_PaymentInfo.getPaymentInfo().getId(), User_PaymentInfo.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(existingUser_PaymentInfo.getPaymentAddress(), response.getBody().getPaymentAddress());
    }

    @Test
    void getRequest_All()
    {
        TestRestTemplate restTemplate = DefaultUser.createTestRestTemplateWithJwt();
        ResponseEntity<User_PaymentInfo[]> response =
                restTemplate.getForEntity(BASE_URL, User_PaymentInfo[].class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().length);
        Assertions.assertEquals(existingUser_PaymentInfo.getPaymentAddress(), response.getBody()[0].getPaymentAddress());
    }

    @Test
    void postRequest()
    {
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setName("newTestName");
        PaymentInfo responsePaymentInfo = paymentInfoRepository.save(paymentInfo);

        User_PaymentInfo user_paymentInfo = new User_PaymentInfo();
        user_paymentInfo.setUser(existingUser_PaymentInfo.getUser());
        user_paymentInfo.setPaymentInfo(responsePaymentInfo);
        user_paymentInfo.setPaymentAddress("newTestPaymentAddress");

        TestRestTemplate restTemplate = DefaultUser.createTestRestTemplateWithJwt();
        ResponseEntity<User_PaymentInfo> response =
                restTemplate.postForEntity(BASE_URL + "/" + responsePaymentInfo.getId(), user_paymentInfo.getPaymentAddress(), User_PaymentInfo.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(user_paymentInfo.getPaymentAddress(), response.getBody().getPaymentAddress());
    }

    @Test
    void putRequest()
    {
        TestRestTemplate restTemplate = DefaultUser.createTestRestTemplateWithJwt();
        String newPaymentAddress = "newTestPaymentAddress";
        restTemplate.put(BASE_URL + "/" + existingUser_PaymentInfo.getPaymentInfo().getId(), newPaymentAddress);
        ResponseEntity<User_PaymentInfo> response = restTemplate.getForEntity(BASE_URL + "/" + existingUser_PaymentInfo.getPaymentInfo().getId(), User_PaymentInfo.class);

        // Verify the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(newPaymentAddress, response.getBody().getPaymentAddress());
    }

    @Test
    void deleteRequest()
    {
        TestRestTemplate restTemplate = DefaultUser.createTestRestTemplateWithJwt();
        restTemplate.delete(BASE_URL + "/" + existingUser_PaymentInfo.getPaymentInfo().getId());
        User_PaymentInfo_Id user_paymentInfo_id = new User_PaymentInfo_Id(
                existingUser_PaymentInfo.getUser(),
                existingUser_PaymentInfo.getPaymentInfo());
        Assertions.assertFalse(user_paymentInfoRepository.existsById(user_paymentInfo_id));
    }
}
