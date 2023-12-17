package org.deustomed.authentication;

import org.deustomed.UserType;
import org.deustomed.postgrest.PostgrestQuery;
import org.deustomed.postgrest.authentication.exceptions.PostgrestAuthenticationException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserAuthenticationServiceTest {
    static UserAuthenticationService userAuthenticationService;

    @BeforeAll
    static void setUp() {
        userAuthenticationService = new UserAuthenticationService("https://localhost:8443", new BypassTrustManager());
    }

    @Test
    @Order(1)
    void notLoggedIn() {
        assertThrows(PostgrestAuthenticationException.class,
                () -> userAuthenticationService.addAuthenticationHeaders(new PostgrestQuery()));
        assertFalse(userAuthenticationService.isLoggedIn());
    }

    @Test
    @Order(2)
    void login() {
        assertDoesNotThrow(() -> userAuthenticationService.login("00AAA", "test", UserType.PATIENT));
        assertNotNull(userAuthenticationService.getSessionId());
        assertNotNull(userAuthenticationService.getAccessToken());
        assertNotNull(userAuthenticationService.getRefreshToken());
        assertNotNull(userAuthenticationService.getExpiresAt());
        assertTrue(userAuthenticationService.isLoggedIn());
    }

    @Test
    @Order(3)
    void logout() {
        userAuthenticationService.logout();
        notLoggedIn();
        assertNull(userAuthenticationService.getSessionId());
        assertNull(userAuthenticationService.getAccessToken());
        assertNull(userAuthenticationService.getRefreshToken());
        assertNull(userAuthenticationService.getExpiresAt());
    }

    @Test
    void addAuthenticationHeaders() {
        assertTrue(userAuthenticationService.isLoggedIn());
        PostgrestQuery postgrestQuery = new PostgrestQuery();
        userAuthenticationService.addAuthenticationHeaders(postgrestQuery);
        assertTrue(postgrestQuery.getHeader("sessionId").contains(userAuthenticationService.getSessionId()));
        assertTrue(postgrestQuery.getHeader("accessToken").contains(userAuthenticationService.getAccessToken()));
    }
}