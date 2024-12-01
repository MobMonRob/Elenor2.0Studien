package de.dhbw.elinor2;

import de.dhbw.elinor2.entities.User;
import de.dhbw.elinor2.repositories.UserRepository;
import de.dhbw.elinor2.utils.TestObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserTest extends GenericTest<User, UUID>
{
    @Autowired
    private UserRepository userRepository;

    private String BASE_URL = "http://localhost:8080/api/users";


    @Override
    public String getObjectAssertionIdentification(User user)
    {
        return user.getUsername() + user.getFirstName() + user.getLastName();
    }

    @Override
    public TestObject<User, UUID> initTestObject()
    {
        TestObject<User, UUID> testObject = new TestObject<>();
        testObject.setEntityClass(User.class);
        testObject.setEntityArrayClass(User[].class);
        testObject.setRepository(userRepository);
        testObject.setBaseUrl(BASE_URL);

        User user = new User();
        user.setUsername("testUsername");
        user.setFirstName("testFirstname");
        user.setLastName("testLastname");
        user = userRepository.save(user);
        testObject.setInitEntity(user);
        testObject.setInitPathId(user.getId());

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
}
