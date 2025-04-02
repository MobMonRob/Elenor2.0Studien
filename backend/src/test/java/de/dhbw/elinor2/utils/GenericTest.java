package de.dhbw.elinor2.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

@Import(TestSecurityConfig.class)
public abstract class GenericTest<ReceivedEntity, SavedEntity, SendEntity, Id> implements IGenericTest
{
    protected TestObject<ReceivedEntity, SavedEntity, SendEntity, Id> testObject;

    protected SavedEntity existingEntity;

    @Override
    @BeforeEach
    public void addTestData()
    {
        testObject = initTestObject();
        existingEntity = testObject.getInitSavedEntity();
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
        TestRestTemplate restTemplate = DefaultUser.createTestRestTemplateWithJwt();
        ResponseEntity<SendEntity> response =
                restTemplate.getForEntity(testObject.getBaseUrl() + "/" +
                        testObject.getInitPathId(), testObject.getEntityClass());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(getObjectAssertionIdentificationSavedEntity(existingEntity), getObjectAssertionIdentificationSendEntity(response.getBody()));
    }

    @Override
    @Test
    public void getRequest_All()
    {
        TestRestTemplate restTemplate = DefaultUser.createTestRestTemplateWithJwt();
        ResponseEntity<SendEntity[]> response =
                restTemplate.getForEntity(testObject.getBaseUrl(), testObject.getEntityArrayClass());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(1, response.getBody().length);
        Assertions.assertEquals(getObjectAssertionIdentificationSavedEntity(existingEntity), getObjectAssertionIdentificationSendEntity(response.getBody()[0]));
    }

    @Override
    @Test
    public void postRequest()
    {
        TestRestTemplate restTemplate = DefaultUser.createTestRestTemplateWithJwt();
        ResponseEntity<SendEntity> response = restTemplate.postForEntity(testObject.getBaseUrl(), testObject.getNewEntity(), testObject.getEntityClass());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(getObjectAssertionIdentificationReceivedEntity(testObject.getNewEntity()), getObjectAssertionIdentificationSendEntity(response.getBody()));
    }

    @Override
    @Test
    public void putRequest()
    {
        TestRestTemplate restTemplate = DefaultUser.createTestRestTemplateWithJwt();

        // Send a PUT request to update the Extern
        restTemplate.put(testObject.getBaseUrl() + "/" + testObject.getInitPathId(), testObject.getUpdateEntity());

        // Send a GET request to retrieve the updated Extern
        ResponseEntity<SendEntity> response = restTemplate.getForEntity(testObject.getBaseUrl() + "/" + testObject.getInitPathId(), testObject.getEntityClass());

        // Verify the response
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(getObjectAssertionIdentificationReceivedEntity(testObject.getUpdateEntity()), getObjectAssertionIdentificationSendEntity(response.getBody()));
    }

    @Override
    @Test
    public void deleteRequest()
    {
        TestRestTemplate restTemplate = DefaultUser.createTestRestTemplateWithJwt();
        restTemplate.delete(testObject.getBaseUrl() + "/" + testObject.getInitPathId());
        Assertions.assertFalse(testObject.getRepository().existsById(testObject.getInitSavedEntityId()));
    }

    public abstract String getObjectAssertionIdentificationSavedEntity(SavedEntity entity);

    public abstract String getObjectAssertionIdentificationReceivedEntity(ReceivedEntity entity);

    public abstract String getObjectAssertionIdentificationSendEntity(SendEntity entity);

    public abstract TestObject<ReceivedEntity, SavedEntity, SendEntity, Id> initTestObject();
}
