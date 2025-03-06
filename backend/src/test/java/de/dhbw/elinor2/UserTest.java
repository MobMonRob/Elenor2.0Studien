package de.dhbw.elinor2;

import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.utils.DefaultUser;
import de.dhbw.elinor2.utils.GenericTest;
import de.dhbw.elinor2.utils.TestObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserTest extends GenericTest<User, User, UUID>
{
    @Autowired
    private UserRepository userRepository;

    private final String BASE_URL = "http://localhost:8080/api/users";


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
    public TestObject<User, User, UUID> initTestObject()
    {
        TestObject<User, User, UUID> testObject = new TestObject<>();
        testObject.setEntityClass(User.class);
        testObject.setEntityArrayClass(User[].class);
        testObject.setRepository(userRepository);
        testObject.setBaseUrl(BASE_URL);

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
    @Disabled("Endpoint not supported")
    @Test
    public void putRequest()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    @Disabled("Endpoint not supported")
    @Test
    public void postRequest()
    {
        throw new UnsupportedOperationException();
    }
}
