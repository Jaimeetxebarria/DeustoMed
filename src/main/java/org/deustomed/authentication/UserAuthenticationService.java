package org.deustomed.authentication;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.deustomed.UserType;
import org.deustomed.postgrest.PostgrestQuery;
import org.deustomed.postgrest.authentication.PostgrestAuthenticationService;
import org.deustomed.postgrest.authentication.exceptions.*;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.lang.ref.Cleaner;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class UserAuthenticationService implements PostgrestAuthenticationService, AutoCloseable {
    private static final Cleaner cleaner = Cleaner.create();
    private final Gson gson = new Gson();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    private final HttpClient client;
    private final String baseUrl;
    private final String anonymousToken;
    private String sessionId;
    private String accessToken;
    private String refreshToken;
    private OffsetDateTime expiresAt;
    private final AuthCleaner authCleaner;
    private final Cleaner.Cleanable cleanable;

    private boolean loggedIn = false;

    /**
     * Class used to log out of the session when the service garbage collected
     */
    private static class AuthCleaner implements Runnable {

        private final HttpClient client;
        private final String baseUrl;
        String sessionId;
        String accessToken;
        boolean isLoggedIn;

        public AuthCleaner(UserAuthenticationService userAuthenticationService) {
            this.client = userAuthenticationService.client;
            this.baseUrl = userAuthenticationService.baseUrl;
            this.sessionId = userAuthenticationService.sessionId;
            this.accessToken = userAuthenticationService.accessToken;
            this.isLoggedIn = userAuthenticationService.isLoggedIn();
        }

        @Override
        public void run() {
            if (!isLoggedIn) return;

            JsonObject postJson = new JsonObject();
            postJson.addProperty("sessionId", sessionId);
            postJson.addProperty("accessToken", accessToken);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + "/logout"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(postJson.toString()))
                    .build();

            try {
                client.send(request, HttpResponse.BodyHandlers.discarding());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @param baseUrl        The base URL of the authentication server.
     * @param trustManager   A trust manager that trusts the authentication server's certificate.
     * @param anonymousToken The anonymous token to use for the client.
     */
    public UserAuthenticationService(@NotNull String baseUrl, TrustManager trustManager, String anonymousToken) {
        this.baseUrl = baseUrl;
        this.anonymousToken = anonymousToken;

        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }

        client = HttpClient.newBuilder()
                .sslContext(sslContext)
                .build();

        authCleaner = new AuthCleaner(this);
        cleanable = cleaner.register(this, authCleaner);
    }

    public void login(String userId, String password, UserType userType) throws PostgrestAuthenticationException {

        JsonObject postJson = new JsonObject();
        postJson.addProperty("id", userId);
        postJson.addProperty("password", password);
        postJson.addProperty("userType", userType.toString());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(postJson.toString()))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new AuthenticationServerUnavailableException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        switch (response.statusCode()) {
            case 200 -> {
                JsonObject responseJson = gson.fromJson(response.body(), JsonObject.class);
                sessionId = authCleaner.sessionId = responseJson.get("sessionId").getAsString();
                accessToken = authCleaner.accessToken = responseJson.get("accessToken").getAsString();
                refreshToken = responseJson.get("refreshToken").getAsString();
                expiresAt = OffsetDateTime.parse(responseJson.get("expiresAt").getAsString(), dateTimeFormatter);
                loggedIn = authCleaner.isLoggedIn = true;
            }
            case 400 -> throw new PostgrestAuthenticationException(response.body());
            case 401 -> throw new InvalidCredentialsException();
            case 404 -> throw new InexistentUserException();
            case 500 -> throw new AuthenticationServerInternalErrorException();
            default -> throw new RuntimeException("Unexpected response code: " + response.statusCode());
        }
    }


    private void refreshSession() throws PostgrestAuthenticationException {
        JsonObject postJson = new JsonObject();
        postJson.addProperty("sessionId", sessionId);
        postJson.addProperty("refreshToken", refreshToken);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/refresh"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(postJson.toString()))
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new AuthenticationServerUnavailableException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        switch (response.statusCode()) {
            case 200 -> {
                JsonObject responseJson = gson.fromJson(response.body(), JsonObject.class);
                accessToken = authCleaner.accessToken = responseJson.get("accessToken").getAsString();
                refreshToken = authCleaner.accessToken = responseJson.get("refreshToken").getAsString();
                expiresAt = OffsetDateTime.parse(responseJson.get("expiresAt").getAsString(), dateTimeFormatter);
            }
            case 400 -> throw new PostgrestAuthenticationException(response.body());
            case 401 -> throw new InvalidCredentialsException(response.body());
            case 500 -> throw new AuthenticationServerInternalErrorException();
            default -> throw new RuntimeException("Unexpected response code: " + response.statusCode());
        }
    }

    public void logout() {
        if (!isLoggedIn()) return;

        JsonObject postJson = new JsonObject();
        postJson.addProperty("sessionId", sessionId);
        postJson.addProperty("accessToken", accessToken);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/logout"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(postJson.toString()))
                .build();

        try {
            client.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (IOException e) {
            throw new AuthenticationServerUnavailableException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        loggedIn = authCleaner.isLoggedIn = false;

        // Clear session data just in case.
        sessionId = authCleaner.sessionId = null;
        accessToken = authCleaner.accessToken = null;
        refreshToken = null;
        expiresAt = null;
    }

    @Override
    public void addAuthenticationHeaders(PostgrestQuery postgrestQuery) throws PostgrestAuthenticationException {
        if (!loggedIn) throw new PostgrestAuthenticationException("Not logged in");

        if (expiresAt.isBefore(OffsetDateTime.now().plusSeconds(10))) { //Add 10 seconds to account for network latency
            refreshSession();
        }

        postgrestQuery.addHeader("apikey", anonymousToken);
        postgrestQuery.addHeader("cookie", "sessionId=" + sessionId);
        postgrestQuery.addHeader("cookie", "accessToken=" + accessToken);
    }

    boolean isLoggedIn() {
        return loggedIn;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    @Override
    public void close() throws Exception {
        cleanable.clean();
    }
}
