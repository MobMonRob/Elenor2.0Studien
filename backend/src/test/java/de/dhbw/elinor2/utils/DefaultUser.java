package de.dhbw.elinor2.utils;

import de.dhbw.elinor2.entities.User;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Date;
import java.util.UUID;

public class DefaultUser
{
    public static final String ID = "2969a04f-0a1f-4e5c-bd77-3071df532d1c";
    public static final String USERNAME = "testusername";
    public static final String FIRST_NAME = "testFirstName";
    public static final String LAST_NAME = "testLastName";

    public static Jwt getJwtToken() {
        return Jwt.withTokenValue(USERNAME)
                .subject(ID)
                .issuedAt(new Date().toInstant())
                .expiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60).toInstant()) // 1 Stunde gültig
                .headers(h -> h.put("alg", "RS256"))
                .claim("preferred_username", USERNAME)
                .claim("given_name", FIRST_NAME)
                .claim("family_name", LAST_NAME)
                .build();
    }

    public static User getDefaultUser() {
        User user = new User();
        user.setId(UUID.fromString(ID));
        user.setUsername(USERNAME);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        return user;
    }

    public static TestRestTemplate createTestRestTemplateWithJwt() {
        Jwt jwt = getJwtToken();

        TestRestTemplate testRestTemplate = new TestRestTemplate();
        testRestTemplate.getRestTemplate().getInterceptors().add(new JwtInterceptor(jwt.getTokenValue()));

        return testRestTemplate;
    }
}
