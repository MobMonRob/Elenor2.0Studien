package de.dhbw.elinor2;

import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.utils.DefaultUser;
import de.dhbw.elinor2.utils.GenericTest;
import de.dhbw.elinor2.utils.TestObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserTest extends GenericTest<User, User, User, UUID>
{
    @Autowired
    private UserRepository userRepository;

    @Override
    public String getObjectAssertionIdentificationSavedEntity(User user)
    {
        return user.getUsername() + user.getFirstName() + user.getLastName();
    }

    @Override
    public String getObjectAssertionIdentificationReceivedEntity(User user)
    {
        return getObjectAssertionIdentificationSavedEntity(user);
    }

    @Override
    public String getObjectAssertionIdentificationSendEntity(User user)
    {
        return getObjectAssertionIdentificationSavedEntity(user);
    }

    @Override
    public TestObject<User, User, User, UUID> initTestObject()
    {
        String baseUrl = "http://localhost:8080/api/users";
        TestObject<User, User, User, UUID> testObject = new TestObject<>();
        testObject.setEntityClass(User.class);
        testObject.setEntityArrayClass(User[].class);
        testObject.setRepository(userRepository);
        testObject.setBaseUrl(baseUrl);

        User user = DefaultUser.getDefaultUser();
        user = userRepository.save(user);
        testObject.setInitSavedEntity(user);
        testObject.setInitSavedEntityId(user.getId());

        User updated = new User();
        updated.setUsername("updatedUsername");
        updated.setFirstName("updatedFirstname");
        updated.setLastName("updatedLastname");
        updated.setId(user.getId());
        testObject.setUpdateEntity(updated);

        User newUser = new User();
        newUser.setUsername("newUsername");
        newUser.setFirstName("newFirstname");
        newUser.setLastName("newLastname");
        testObject.setNewEntity(newUser);

        return testObject;
    }

    @Override
    //@Test
    public void putRequest()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    //@Test
    public void postRequest()
    {
        throw new UnsupportedOperationException();
    }

    @Test
    public void updateUserTest()
    {
        TestRestTemplate restTemplate = DefaultUser.createTestRestTemplateWithJwt();
        ResponseEntity<User> response = restTemplate.postForEntity(testObject.getBaseUrl() + "/me", null, testObject.getEntityClass());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(getObjectAssertionIdentificationReceivedEntity(DefaultUser.getDefaultUser()), getObjectAssertionIdentificationSendEntity(response.getBody()));
    }
}
