package org.deustomed.authentication;

import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestClientFactory;
import org.deustomed.postgrest.PostgrestQuery;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.deustomed.postgrest.PostgrestAssertions.assertDatabaseUserEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SuperuserAuthenticationServiceTest {
    private static PostgrestClient postgrestClient;
    private static SuperuserAuthenticationService superuserAuthenticationService;

    @BeforeAll
    static void setUp() {
        postgrestClient = PostgrestClientFactory.createSuperuserClient();
        superuserAuthenticationService = (SuperuserAuthenticationService) postgrestClient.getAuthenticationService();
    }

    @Test
    void addAuthenticationHeaders() {
        PostgrestQuery postgrestQuery = new PostgrestQuery();
        superuserAuthenticationService.addAuthenticationHeaders(postgrestQuery);
        assertNull(postgrestQuery.getHeader("Authorization"));
        assertTrue(postgrestQuery.getHeader("apikey").contains(PostgrestClientFactory.getProperty("anonymousToken")));
        Optional.ofNullable(postgrestQuery.getHeader("cookie"))
                .ifPresent(values -> assertTrue(values.stream().noneMatch(s -> s.startsWith("sessionId="))));
        assertTrue(postgrestQuery.getHeader("cookie").contains("accessToken=" + PostgrestClientFactory.getProperty("superuserToken")));
    }

    @Test
    void correctDatabaseUser() {
        assertDatabaseUserEquals("service_role", postgrestClient);
    }
}