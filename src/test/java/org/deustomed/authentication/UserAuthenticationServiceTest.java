package org.deustomed.authentication;

import org.deustomed.UserType;
import org.deustomed.postgrest.PostgrestQuery;
import org.deustomed.postgrest.authentication.exceptions.InexistentUserException;
import org.deustomed.postgrest.authentication.exceptions.InvalidCredentialsException;
import org.deustomed.postgrest.authentication.exceptions.PostgrestAuthenticationException;
import org.junit.jupiter.api.*;

import java.lang.reflect.Method;
import java.time.OffsetDateTime;

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
        assertThrows(InvalidCredentialsException.class,
                () -> userAuthenticationService.login("00AAA", "wrong password", UserType.PATIENT));

        assertThrows(InexistentUserException.class,
                () -> userAuthenticationService.login("00AAA", "1234E?", UserType.PATIENT));

        assertDoesNotThrow(() -> userAuthenticationService.login("00AAA", "1234E?", UserType.DOCTOR));

        assertNotNull(userAuthenticationService.getSessionId());
        assertNotNull(userAuthenticationService.getAccessToken());
        assertNotNull(userAuthenticationService.getRefreshToken());
        assertNotNull(userAuthenticationService.getExpiresAt());
        assertTrue(userAuthenticationService.isLoggedIn());
    }

    @Test
    @Order(3)
    void refreshSession() {
        String sessionIdBefore = userAuthenticationService.getSessionId();
        String accessTokenBefore = userAuthenticationService.getAccessToken();
        String refreshTokenBefore = userAuthenticationService.getRefreshToken();
        OffsetDateTime expiresAtBefore = userAuthenticationService.getExpiresAt();

        // Use reflection to call the private method refreshSession()
        Method method;
        try {
            method = UserAuthenticationService.class.getDeclaredMethod("refreshSession");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        method.setAccessible(true);
        assertDoesNotThrow(() -> method.invoke(userAuthenticationService));

        assertNotNull(userAuthenticationService.getSessionId());
        assertNotNull(userAuthenticationService.getAccessToken());
        assertNotNull(userAuthenticationService.getRefreshToken());
        assertNotNull(userAuthenticationService.getExpiresAt());
        assertTrue(userAuthenticationService.isLoggedIn());

        // Make sure the session data was updated
        assertEquals(sessionIdBefore, userAuthenticationService.getSessionId());
        assertNotEquals(accessTokenBefore, userAuthenticationService.getAccessToken());
        assertNotEquals(refreshTokenBefore, userAuthenticationService.getRefreshToken());
        assertNotEquals(expiresAtBefore, userAuthenticationService.getExpiresAt());
        assertTrue(expiresAtBefore.isBefore(userAuthenticationService.getExpiresAt()));
    }

    @Test
    @Order(5)
    void logout() {
        userAuthenticationService.logout();
        notLoggedIn();
        assertNull(userAuthenticationService.getSessionId());
        assertNull(userAuthenticationService.getAccessToken());
        assertNull(userAuthenticationService.getRefreshToken());
        assertNull(userAuthenticationService.getExpiresAt());
    }

    @Test
    @Order(4)
    void addAuthenticationHeaders() {
        assertTrue(userAuthenticationService.isLoggedIn());
        PostgrestQuery postgrestQuery = new PostgrestQuery();
        userAuthenticationService.addAuthenticationHeaders(postgrestQuery);
        assertTrue(postgrestQuery.getHeader("sessionId").contains(userAuthenticationService.getSessionId()));
        assertTrue(postgrestQuery.getHeader("accessToken").contains(userAuthenticationService.getAccessToken()));
    }
}