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

class AnonymousAuthenticationServiceTest {
    private static PostgrestClient postgrestClient;
    static AnonymousAuthenticationService anonymousAuthenticationService;

    @BeforeAll
    static void setUp() {
        postgrestClient = PostgrestClientFactory.createAnonymousClient();
        anonymousAuthenticationService = (AnonymousAuthenticationService) postgrestClient.getAuthenticationService();
    }

    @Test
    void addAuthenticationHeaders() {
        PostgrestQuery postgrestQuery = new PostgrestQuery();
        anonymousAuthenticationService.addAuthenticationHeaders(postgrestQuery);
        assertNull(postgrestQuery.getHeader("Authorization"));

        Optional.ofNullable(postgrestQuery.getHeader("cookie"))
                .ifPresent(values -> assertTrue(values.stream().noneMatch(s -> s.startsWith("sessionId="))));
        Optional.ofNullable(postgrestQuery.getHeader("cookie"))
                .ifPresent(values -> assertTrue(values.stream().noneMatch(s -> s.startsWith("accessToken="))));

        assertTrue(postgrestQuery.getHeader("apikey").contains(PostgrestClientFactory.getProperty("anonymousToken")));
    }

    @Test
    void correctDatabaseUser() {
        assertDatabaseUserEquals("anon", postgrestClient);
    }
}