package de.dhbw.elinor2;
import de.dhbw.elinor2.entities.Extern;
import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.entities.User_PaymentInfo;
import de.dhbw.elinor2.repositories.ExternRepository;
import de.dhbw.elinor2.repositories.UserRepository;
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
public class ExternTest
{
    @Autowired
    private ExternRepository externRepository;

    private Extern existingExtern;

    @BeforeEach
    public void addTestData()
    {
        Extern extern = new Extern();
        extern.setName("testFirstname");
        existingExtern = externRepository.save(extern);
    }

    @AfterEach
    public void deleteTestData()
    {
        externRepository.deleteAll();
    }

    @Test
    void getRequest_Single()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<Extern> response =
            restTemplate.getForEntity("http://localhost:8080/externs/" + existingExtern.getId(), Extern.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(existingExtern.getName(), response.getBody().getName());
    }

    @Test
    void getRequest_All()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<Extern[]> response =
            restTemplate.getForEntity("http://localhost:8080/externs", Extern[].class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().length);
        Assertions.assertEquals(existingExtern.getName(), response.getBody()[0].getName());
    }

    @Test
    void postRequest()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();

        // Create a new Extern object
        Extern newExtern = new Extern();
        newExtern.setName("newTestFirstname");

        ResponseEntity<Extern> response = restTemplate.postForEntity("http://localhost:8080/externs", newExtern, Extern.class);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(newExtern.getName(), response.getBody().getName());
    }

    @Test
    void putRequest()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();

        // Update the Extern object
        existingExtern.setName("updatedTestFirstname");

        // Send a PUT request to update the Extern
        restTemplate.put("http://localhost:8080/externs/" + existingExtern.getId(), existingExtern);

        // Send a GET request to retrieve the updated Extern
        ResponseEntity<Extern> response = restTemplate.getForEntity("http://localhost:8080/externs/" + existingExtern.getId(), Extern.class);

        // Verify the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(existingExtern.getName(), response.getBody().getName());
    }

    @Test
    void deleteRequest()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();

        // Send a DELETE request to delete the Extern
        restTemplate.delete("http://localhost:8080/externs/" + existingExtern.getId());

        Assertions.assertFalse(externRepository.existsById(existingExtern.getId()));
    }
}
