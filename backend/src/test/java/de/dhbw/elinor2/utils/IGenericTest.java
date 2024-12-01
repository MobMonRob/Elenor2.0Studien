package de.dhbw.elinor2.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public interface IGenericTest
{
    @BeforeEach
    void addTestData();

    @AfterEach
    void deleteTestData();

    @Test
    void getRequest_Single();

    @Test
    void getRequest_All();

    @Test
    void postRequest();

    @Test
    void putRequest();

    @Test
    void deleteRequest();
}
