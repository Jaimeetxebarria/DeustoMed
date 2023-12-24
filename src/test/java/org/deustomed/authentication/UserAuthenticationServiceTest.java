package org.deustomed.authentication;

import com.google.gson.JsonElement;
import org.deustomed.UserType;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestClientFactory;
import org.deustomed.postgrest.PostgrestQuery;
import org.deustomed.postgrest.authentication.exceptions.InexistentUserException;
import org.deustomed.postgrest.authentication.exceptions.InvalidCredentialsException;
import org.deustomed.postgrest.authentication.exceptions.PostgrestAuthenticationException;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.time.OffsetDateTime;

import static org.deustomed.postgrest.PostgrestAssertions.assertDatabaseUserEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserAuthenticationServiceTest {
    static UserAuthenticationService userAuthenticationService;
    static PostgrestClient postgrestClient;

    private static final String HOSTNAME = "localhost";
    private static final int PORT = 8443;


    static boolean isAuthServerAvailable() {
        try (Socket s = new Socket(HOSTNAME, PORT)) {
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    boolean sessionExists(String sessionId) {
        PostgrestClient superuserClient = PostgrestClientFactory.createSuperuserClient();
        PostgrestQuery postgrestQuery = superuserClient.from("session").select().eq("id", sessionId).getQuery();
        postgrestQuery.addHeader("Accept-Profile", "custom_auth"); // Set the schema TODO: Update this when schema selection is implemented
        JsonElement responseJson = superuserClient.sendQuery(postgrestQuery);
        return responseJson.isJsonArray() && responseJson.getAsJsonArray().size() == 1;
    }

    @BeforeAll
    static void setUp() {
        assumeTrue(isAuthServerAvailable(), "Authentication server not available"); // Skip tests if the auth server is not available

        userAuthenticationService = new UserAuthenticationService("https://" + HOSTNAME + ":" + PORT, new BypassTrustManager(),
                PostgrestClientFactory.getProperty("anonymousToken"));
        postgrestClient = PostgrestClientFactory.createAuthenticatedClient(userAuthenticationService);
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

        assertTrue(sessionExists(userAuthenticationService.getSessionId()));
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
    @Order(6)
    void logout() {
        String sessionId = userAuthenticationService.getSessionId();

        userAuthenticationService.logout();
        notLoggedIn();
        assertNull(userAuthenticationService.getSessionId());
        assertNull(userAuthenticationService.getAccessToken());
        assertNull(userAuthenticationService.getRefreshToken());
        assertNull(userAuthenticationService.getExpiresAt());

        assertFalse(sessionExists(sessionId));
    }

    @Test
    @Order(4)
    void addAuthenticationHeaders() {
        assertTrue(userAuthenticationService.isLoggedIn());
        PostgrestQuery postgrestQuery = new PostgrestQuery();
        userAuthenticationService.addAuthenticationHeaders(postgrestQuery);
        assertTrue(postgrestQuery.getHeader("apikey").contains(PostgrestClientFactory.getProperty("anonymousToken")));
        assertTrue(postgrestQuery.getHeader("cookie").contains("sessionId=" + userAuthenticationService.getSessionId()));
        assertTrue(postgrestQuery.getHeader("cookie").contains("accessToken=" + userAuthenticationService.getAccessToken()));
    }

    @Test
    @Order(5)
    void correctDatabaseUser() {
        assertDatabaseUserEquals("authenticated", postgrestClient);
    }


    @Test
    void logoutWhenGarbageCollected() throws InterruptedException {
        UserAuthenticationService authenticationService = new UserAuthenticationService("https://" + HOSTNAME + ":" + PORT,
                new BypassTrustManager(),
                PostgrestClientFactory.getProperty("anonymousToken"));

        authenticationService.login("00AAA", "1234E?", UserType.DOCTOR);
        String sessionId = authenticationService.getSessionId();

        assertTrue(sessionExists(sessionId));

        // Forget about the only object reference to force garbage collection
        authenticationService = null;
        System.gc();

        Thread.sleep(500); // Wait for the garbage collector (Just in case)
        assertFalse(sessionExists(sessionId));
    }

    @Test
    void logoutTryWithResources() {
        assertDoesNotThrow(() -> {
            String sessionId;

            try (UserAuthenticationService authenticationService = new UserAuthenticationService("https://" + HOSTNAME + ":" + PORT,
                    new BypassTrustManager(),
                    PostgrestClientFactory.getProperty("anonymousToken"))) {
                authenticationService.login("00AAA", "1234E?", UserType.DOCTOR);
                sessionId = authenticationService.getSessionId();

                assertTrue(sessionExists(sessionId));
            }

            Thread.sleep(500); // Wait for the garbage collector (Just in case)
            assertFalse(sessionExists(sessionId));
        });
    }

}