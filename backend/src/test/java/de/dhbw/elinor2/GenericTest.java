package de.dhbw.elinor2;

import de.dhbw.elinor2.utils.TestObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class GenericTest<Entity, Id> implements IGenericTest
{
    private TestObject<Entity, Id> testObject;

    private Entity existingEntity;

    @Override
    @BeforeEach
    public void addTestData()
    {
        testObject = initTestObject();
        existingEntity = testObject.getRepository().save(testObject.getInitEntity());
    }

    @Override
    @AfterEach
    public void deleteTestData()
    {
        testObject.getRepository().deleteAll();
    }

    @Override
    @Test
    public void getRequest_Single()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<Entity> response =
                restTemplate.getForEntity(testObject.getBaseUrl() + "/" +
                        testObject.getInitPathId(), testObject.getEntityClass());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(getObjectAssertionIdentification(existingEntity), getObjectAssertionIdentification(response.getBody()));
    }

    @Override
    @Test
    public void getRequest_All()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<Entity[]> response =
                restTemplate.getForEntity(testObject.getBaseUrl(), testObject.getEntityArrayClass());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().length);
        Assertions.assertEquals(getObjectAssertionIdentification(existingEntity), getObjectAssertionIdentification(response.getBody()[0]));
    }

    @Override
    @Test
    public void postRequest()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        ResponseEntity<Entity> response = restTemplate.postForEntity(testObject.getBaseUrl(), testObject.getNewEntity(), testObject.getEntityClass());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(getObjectAssertionIdentification(testObject.getNewEntity()), getObjectAssertionIdentification(response.getBody()));
    }

    @Override
    @Test
    public void putRequest()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();

        // Send a PUT request to update the Extern
        restTemplate.put(testObject.getBaseUrl() + "/" + testObject.getInitPathId(), testObject.getUpdateEntity());

        // Send a GET request to retrieve the updated Extern
        ResponseEntity<Entity> response = restTemplate.getForEntity(testObject.getBaseUrl() + "/" + testObject.getInitPathId(), testObject.getEntityClass());

        // Verify the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(getObjectAssertionIdentification(testObject.getUpdateEntity()), getObjectAssertionIdentification(response.getBody()));
    }

    @Override
    @Test
    public void deleteRequest()
    {
        TestRestTemplate restTemplate = new TestRestTemplate();
        restTemplate.delete(testObject.getBaseUrl() + "/" + testObject.getInitPathId());
        Assertions.assertFalse(testObject.getRepository().existsById(testObject.getInitEntityId()));
    }

    public abstract String getObjectAssertionIdentification(Entity entity);

    public abstract TestObject<Entity, Id> initTestObject();
}
