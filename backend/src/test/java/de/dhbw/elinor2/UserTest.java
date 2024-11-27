package de.dhbw.elinor2;
import de.dhbw.elinor2.entities.User;
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
public class UserTest
{
    @Autowired
    private UserRepository userRepository;

    private User existingUser;

    @BeforeEach
    public void addTestData()
    {
        User user = new User();
        user.setUsername("testUsername");
        user.setFirstName("testFirstname");
        user.setLastName("testLastname");
        existingUser = userRepository.save(user);
    }

    @AfterEach
    public void deleteTestData()
    {
        userRepository.deleteAll();
    }


    @Test
    void getRequest_Single()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<User> response =
            restTemplate.getForEntity("http://localhost:8080/users/" + existingUser.getId(), User.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(existingUser.getUsername(), response.getBody().getUsername());
    }

    @Test
    void getRequest_All()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<User[]> response =
            restTemplate.getForEntity("http://localhost:8080/users", User[].class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().length);
        Assertions.assertEquals(existingUser.getUsername(), response.getBody()[0].getUsername());
    }

    @Test
    void postRequest()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();

        // Create a new User object
        User newUser = new User();
        newUser.setUsername("newTestUsername");
        newUser.setFirstName("newTestFirstname");
        newUser.setLastName("newTestLastname");

        // Send a POST request to create the new user
        ResponseEntity<User> response = restTemplate.postForEntity("http://localhost:8080/users", newUser, User.class);

        // Verify the response
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(newUser.getUsername(), response.getBody().getUsername());
    }

    @Test
    void putRequest()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();

        // Modify the existing user
        existingUser.setFirstName("updatedFirstName");

        // Send a PUT request to update the user
        restTemplate.put ("http://localhost:8080/users/" + existingUser.getId(), existingUser);

        ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:8080/users/" + existingUser.getId(), User.class);

        // Verify the response
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getFirstName(), "updatedFirstName");
    }

    @Test
    void deleteRequest()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();

        // Send a DELETE request to remove the user
        restTemplate.delete("http://localhost:8080/users/" + existingUser.getId());

        // Verify the user has been deleted
        ResponseEntity<User> response = restTemplate.getForEntity("http://localhost:8080/users/" + existingUser.getId(), User.class);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
