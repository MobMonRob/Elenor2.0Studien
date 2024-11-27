package de.dhbw.elinor2;
import de.dhbw.elinor2.entities.PaymentInfo;
import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.User_PaymentInfo;
import de.dhbw.elinor2.entities.User_PaymentInfo_Id;
import de.dhbw.elinor2.repositories.PaymentInfoRepository;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.repositories.User_PaymentInfoRepository;
import de.dhbw.elinor2.services.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    @BeforeEach
    public void addTestData()
    {
        User user = new User();
        user.setUsername("testUsername");
        user.setFirstName("testFirstname");
        user.setLastName("testLastname");
        User responseUser = userRepository.save(user);

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setName("testName");
        PaymentInfo responsePaymentInfo = paymentInfoRepository.save(paymentInfo);

        existingUser_PaymentInfo = userService.createUserPaymentInfo(
                responseUser.getId(),
                responsePaymentInfo.getId(),
                "testPaymentAddress");
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
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<User_PaymentInfo> response =
            restTemplate.getForEntity("http://localhost:8080/users/" + existingUser_PaymentInfo.getUser().getId() + "/paymentinfos/" + existingUser_PaymentInfo.getPaymentInfo().getId(), User_PaymentInfo.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(existingUser_PaymentInfo.getPaymentAddress(), response.getBody().getPaymentAddress());
    }

    @Test
    void getRequest_All()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<User_PaymentInfo[]> response =
            restTemplate.getForEntity("http://localhost:8080/users/"+ existingUser_PaymentInfo.getUser().getId() +"/paymentinfos", User_PaymentInfo[].class);
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

        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<User_PaymentInfo> response =
            restTemplate.postForEntity("http://localhost:8080/users/" + existingUser_PaymentInfo.getUser().getId() + "/paymentinfos/" + responsePaymentInfo.getId(), user_paymentInfo.getPaymentAddress(), User_PaymentInfo.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(user_paymentInfo.getPaymentAddress(), response.getBody().getPaymentAddress());
    }

    @Test
    void putRequest()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        String newPaymentAddress = "newTestPaymentAddress";
        restTemplate.put("http://localhost:8080/users/" + existingUser_PaymentInfo.getUser().getId() + "/paymentinfos/" + existingUser_PaymentInfo.getPaymentInfo().getId(), newPaymentAddress);
        ResponseEntity<User_PaymentInfo> response = restTemplate.getForEntity("http://localhost:8080/users/" + existingUser_PaymentInfo.getUser().getId() + "/paymentinfos/" + existingUser_PaymentInfo.getPaymentInfo().getId(), User_PaymentInfo.class);

        // Verify the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(newPaymentAddress, response.getBody().getPaymentAddress());
    }

    @Test
    void deleteRequest()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        restTemplate.delete("http://localhost:8080/users/" + existingUser_PaymentInfo.getUser().getId() + "/paymentinfos/" + existingUser_PaymentInfo.getPaymentInfo().getId());
        User_PaymentInfo_Id user_paymentInfo_id = new User_PaymentInfo_Id(
                existingUser_PaymentInfo.getUser(),
                existingUser_PaymentInfo.getPaymentInfo());
        Assertions.assertFalse(user_paymentInfoRepository.existsById(user_paymentInfo_id));
    }
}
