package org.deustomed.postgrest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.deustomed.httputils.HttpMethod;
import org.deustomed.httputils.UrlBuilder;
import org.deustomed.httputils.UrlScheme;
import org.deustomed.postgrest.authentication.PostgrestAuthenticationService;
import org.jetbrains.annotations.NotNull;

import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class PostgrestClient {
    public static final Gson gson = new Gson();
    private final HttpClient httpClient;
    private final String hostname;
    private final String endpoint;
    private final PostgrestAuthenticationService authenticationService;

    /**
     * @param hostname              The URL of the Postgrest server
     *                              Example: postgrest.example.com
     * @param endpoint              The endpoint of the Postgrest server
     *                              Example: /rest/v1
     * @param authenticationService The authentication service to use
     */
    public PostgrestClient(@NotNull String hostname, String endpoint, @NotNull PostgrestAuthenticationService authenticationService) {
        this.hostname = hostname;
        this.endpoint = endpoint;

        this.httpClient = HttpClient.newBuilder().build();
        this.authenticationService = authenticationService;
    }

    public JsonElement sendQuery(@NotNull PostgrestQuery query) {
        HttpRequest.Builder requestBuilder;

        //Create the initial request uri
        try {
            requestBuilder = HttpRequest.newBuilder().uri(query.getUrlBuilder().build().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        //Add authentication headers
        authenticationService.addAuthenticationHeaders(query);

        // Set the request method and body (if applicable)
        switch (query.getHttpMethod()) {
            case GET -> requestBuilder.GET();
            case POST -> requestBuilder.POST(HttpRequest.BodyPublishers.ofString(query.getBody().toString()));
            case PUT -> requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(query.getBody().toString()));
            case PATCH -> requestBuilder.method("PATCH", HttpRequest.BodyPublishers.ofString(query.getBody().toString()));
            case DELETE -> requestBuilder.DELETE();
        }

        //Set the request headers (copy from PostgrestQuery)
        for (Map.Entry<String, List<String>> header : query.getHeaders().entrySet()) {
            for (String value : header.getValue()) {
                requestBuilder.header(header.getKey(), value);
            }
        }

        HttpRequest request = requestBuilder.build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return gson.fromJson(response.body(), JsonElement.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // =================== DATABASE ===================
    public PostgrestQueryBuilder from(@NotNull String table) {
        if (table.isEmpty()) throw new IllegalArgumentException("Cannot query blank table");

        PostgrestQueryBuilder postgrestQueryBuilder = new PostgrestQueryBuilder();
        UrlBuilder urlBuilder = postgrestQueryBuilder.getQuery().getUrlBuilder();
        urlBuilder.setScheme(UrlScheme.HTTPS);
        urlBuilder.setHostname(hostname);
        urlBuilder.setPath(endpoint + '/' + table);

        return postgrestQueryBuilder;
    }

    // ========== REMOTE PROCEDURE CALLS (RPC) ==========

    /**
     * Calls a function with no parameters in the PostgreSQL database.
     *
     * @param procedureName The name of the procedure to call
     * @return The response from the server
     */
    public PostgrestFilterBuilder rpc(@NotNull String procedureName) {
        PostgrestQuery postgrestQuery = new PostgrestQuery();

        UrlBuilder urlBuilder = postgrestQuery.getUrlBuilder();
        urlBuilder.setScheme(UrlScheme.HTTPS);
        urlBuilder.setHostname(hostname);
        urlBuilder.setPath(endpoint + "/rpc/" + procedureName);

        postgrestQuery.setHttpMethod(HttpMethod.GET);

        return new PostgrestFilterBuilder(postgrestQuery);
    }

    /**
     * Calls a function with parameters in the PostgreSQL database.
     *
     * @param procedureName The name of the procedure to call
     * @param parameters    The parameters to pass to the procedure
     * @return The response from the server
     */
    public PostgrestFilterBuilder rpc(@NotNull String procedureName, @NotNull JsonObject parameters) {
        if (procedureName.isEmpty()) throw new IllegalArgumentException("Cannot call procedure with blank name");

        PostgrestQuery postgrestQuery = new PostgrestQuery();

        UrlBuilder urlBuilder = postgrestQuery.getUrlBuilder();
        urlBuilder.setScheme(UrlScheme.HTTPS);
        urlBuilder.setHostname(hostname);
        urlBuilder.setPath(endpoint + "/rpc/" + procedureName);

        postgrestQuery.setHttpMethod(HttpMethod.POST);
        postgrestQuery.setBody(parameters);

        return new PostgrestFilterBuilder(postgrestQuery);
    }

    /**
     * Calls a function with parameters in the PostgreSQL database.
     *
     * @param procedureName The name of the procedure to call
     * @param parameters    The parameters to pass to the procedure
     * @return The response from the server
     */
    public PostgrestFilterBuilder rpc(@NotNull String procedureName, @NotNull Entry<?>... parameters) {
        if (procedureName.isEmpty()) throw new IllegalArgumentException("Cannot call procedure with blank name");

        PostgrestQuery postgrestQuery = new PostgrestQuery();

        UrlBuilder urlBuilder = postgrestQuery.getUrlBuilder();
        urlBuilder.setScheme(UrlScheme.HTTPS);
        urlBuilder.setHostname(hostname);
        urlBuilder.setPath(endpoint + "/rpc/" + procedureName);

        postgrestQuery.setHttpMethod(HttpMethod.POST);
        postgrestQuery.setBody(Entry.toJsonObject(parameters));

        return new PostgrestFilterBuilder(postgrestQuery);
    }

    public String getHostname() {
        return hostname;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public PostgrestAuthenticationService getAuthenticationService() {
        return authenticationService;
    }
}
