package org.deustomed.authentication;

import com.google.gson.Gson;
import org.deustomed.UserType;
import org.deustomed.authentication.exceptions.*;

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
import java.util.Map;

public class AuthenticationService {
    private static final String BASE_URL = "https://localhost:8443";
    private final Gson gson = new Gson();
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ"
    );
    private final HttpClient client;
    private String refreshToken;
    private OffsetDateTime expiresAt;

    public AuthenticationService(TrustManager trustManager) {
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

    public void login(String userId, String password, UserType userType) throws AuthenticationException {

        String postData = """
                {
                    "id": "%s",
                    "password": "%s",
                    "userType": "%s"
                }
                """.formatted(userId, password, userType.toString());
        System.out.println(postData);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(postData))
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
                Map<String, Object> responseData = gson.fromJson(response.body(), Map.class);
                responseData.forEach((key, value) -> System.out.println(key + " " + value));
                refreshToken = (String) responseData.get("refreshToken");
                expiresAt = OffsetDateTime.parse((String) responseData.get("expiresAt"), dateTimeFormatter);
            }
            case 400 -> {
                switch (response.body()) {
                    case "Invalid credentials" -> throw new InvalidCredentialsException();
                    case "User does not exist" -> throw new InexistentUserException();
                    default -> throw new AuthenticationException(response.body());
                }
            }
            case 500 -> throw new AuthenticationServerInternalErrorException();
            default -> throw new RuntimeException("Unexpected response code: " + response.statusCode());
        }
    }


    public void refreshSession() throws AuthenticationException {
        String postData = """
                {
                    "refresh_token": "%s"
                }
                """.formatted(refreshToken);
        System.out.println(postData);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/refresh"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(postData))
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
                Map<String, Object> responseData = gson.fromJson(response.body(), Map.class);
                responseData.forEach((key, value) -> System.out.println(key + " " + value));
                refreshToken = (String) responseData.get("refreshToken");
                expiresAt = OffsetDateTime.parse((String) responseData.get("expiresAt"), dateTimeFormatter);
            }
            case 400 -> throw new RuntimeException("Unexpected response code: " + response.statusCode()); //TODO change
            case 500 -> throw new AuthenticationServerInternalErrorException();
            default -> throw new RuntimeException("Unexpected response code: " + response.statusCode());
        }
    }

    public void logout() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/logout"))
                .GET()
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (IOException e) {
            throw new AuthenticationServerUnavailableException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        AuthenticationService authenticationService = new AuthenticationService(new BypassTrustManager());

        try {
            authenticationService.login("05RYN", "1234", UserType.PATIENT);
        } catch (AuthenticationException e) {
            System.out.println(e.getMessage());
        }

    }
}
