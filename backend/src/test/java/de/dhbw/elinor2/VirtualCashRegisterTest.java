package de.dhbw.elinor2;
import de.dhbw.elinor2.entities.VirtualCashRegister;
import de.dhbw.elinor2.repositories.VirtualCashRegisterRepository;
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
public class VirtualCashRegisterTest
{
    @Autowired
    private VirtualCashRegisterRepository virtualCashRegisterRepository;

    private VirtualCashRegister existingVirtualCashRegister;

    @BeforeEach
    public void addTestData()
    {
        VirtualCashRegister virtualCashRegister = new VirtualCashRegister();
        virtualCashRegister.setName("testName");
        existingVirtualCashRegister = virtualCashRegisterRepository.save(virtualCashRegister);
    }

    @AfterEach
    public void deleteTestData()
    {
        virtualCashRegisterRepository.deleteAll();
    }

    @Test
    void getRequest_Single()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<VirtualCashRegister> response =
            restTemplate.getForEntity("http://localhost:8080/virtualcashregisters/" + existingVirtualCashRegister.getId(), VirtualCashRegister.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(existingVirtualCashRegister.getName(), response.getBody().getName());
    }

    @Test
    void getRequest_All()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<VirtualCashRegister[]> response =
            restTemplate.getForEntity("http://localhost:8080/virtualcashregisters", VirtualCashRegister[].class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().length);
        Assertions.assertEquals(existingVirtualCashRegister.getName(), response.getBody()[0].getName());
    }

    @Test
    void postRequest()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();

        // Create a new VirtualCashRegister object
        VirtualCashRegister newVirtualCashRegister = new VirtualCashRegister();
        newVirtualCashRegister.setName("newTestName");

        ResponseEntity<VirtualCashRegister> response = restTemplate.postForEntity("http://localhost:8080/virtualcashregisters", newVirtualCashRegister, VirtualCashRegister.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void putRequest()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();

        // Modify the existing user
        existingVirtualCashRegister.setName("updatedName");

        // Send a PUT request to update the user
        restTemplate.put ("http://localhost:8080/virtualcashregisters/" + existingVirtualCashRegister.getId(), existingVirtualCashRegister);

        ResponseEntity<VirtualCashRegister> response = restTemplate.getForEntity("http://localhost:8080/virtualcashregisters/" + existingVirtualCashRegister.getId(), VirtualCashRegister.class);

        // Verify the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("updatedName", response.getBody().getName());
    }

    @Test
    void deleteRequest()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        restTemplate.delete("http://localhost:8080/virtualcashregisters/" + existingVirtualCashRegister.getId());
        Assertions.assertFalse(virtualCashRegisterRepository.existsById(existingVirtualCashRegister.getId()));
    }

}
