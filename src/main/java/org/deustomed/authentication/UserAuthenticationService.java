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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class UserAuthenticationService implements PostgrestAuthenticationService {
    private final String BASE_URL;
    private final Gson gson = new Gson();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");
    private final HttpClient client;
    String sessionId;
    String accessToken;
    String refreshToken;
    OffsetDateTime expiresAt;

    private boolean loggedIn = false;

    /**
     * @param baseUrl      The base URL of the authentication server.
     * @param trustManager A trust manager that trusts the authentication server's certificate
     */
    public UserAuthenticationService(@NotNull String baseUrl, TrustManager trustManager) {
        BASE_URL = baseUrl;

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
    }

    public void login(String userId, String password, UserType userType) throws PostgrestAuthenticationException {

        JsonObject postJson = new JsonObject();
        postJson.addProperty("id", userId);
        postJson.addProperty("password", password);
        postJson.addProperty("userType", userType.toString());

        System.out.println(postJson);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/login"))
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
                sessionId = responseJson.get("sessionId").getAsString();
                accessToken = responseJson.get("accessToken").getAsString();
                refreshToken = responseJson.get("refreshToken").getAsString();
                expiresAt = OffsetDateTime.parse(responseJson.get("expiresAt").getAsString(), dateTimeFormatter);
                loggedIn = true;
            }
            case 400 -> {
                if (response.body().equals("User does not exist")) {
                    throw new InexistentUserException();
                }
                throw new PostgrestAuthenticationException(response.body());
            }
            case 401 -> throw new InvalidCredentialsException();
            case 500 -> throw new AuthenticationServerInternalErrorException();
            default -> throw new RuntimeException("Unexpected response code: " + response.statusCode());
        }
    }


    private void refreshSession() throws PostgrestAuthenticationException {
        JsonObject postJson = new JsonObject();
        postJson.addProperty("sessionId", sessionId);
        postJson.addProperty("refreshToken", refreshToken);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/refresh"))
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
                accessToken = responseJson.get("accessToken").getAsString();
                refreshToken = responseJson.get("refreshToken").getAsString();
                expiresAt = OffsetDateTime.parse(responseJson.get("expiresAt").getAsString(), dateTimeFormatter);
            }
            case 400, 401 -> throw new InvalidCredentialsException(response.body()); //TODO change
            case 500 -> throw new AuthenticationServerInternalErrorException();
            default -> throw new RuntimeException("Unexpected response code: " + response.statusCode());
        }
    }

    public void logout() {
        JsonObject postJson = new JsonObject();
        postJson.addProperty("sessionId", sessionId);
        postJson.addProperty("accessToken", accessToken);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/logout"))
                .POST(HttpRequest.BodyPublishers.ofString(postJson.toString()))
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (IOException e) {
            throw new AuthenticationServerUnavailableException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        loggedIn = false;
    }

    @Override
    public void addAuthenticationHeaders(PostgrestQuery postgrestQuery) throws PostgrestAuthenticationException {
        if (!loggedIn) throw new PostgrestAuthenticationException("Not logged in");

        if (expiresAt.isBefore(OffsetDateTime.now().plusSeconds(10))) { //Add 10 seconds to account for network latency
            refreshSession();
        }

        postgrestQuery.addHeader("sessionId", sessionId);
        postgrestQuery.addHeader("accessToken", accessToken);
    }

    boolean isLoggedIn() {
        return loggedIn;
    }
}
