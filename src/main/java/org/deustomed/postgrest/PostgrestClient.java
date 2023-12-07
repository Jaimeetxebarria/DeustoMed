package org.deustomed.postgrest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.deustomed.httputils.UrlBuilder;
import org.deustomed.httputils.UrlScheme;
import org.jetbrains.annotations.NotNull;

import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class PostgrestClient {
    private static final Gson gson = new Gson();
    private final HttpClient httpClient;
    private final String hostname;
    private final String endpoint;
    private final String anonymousToken;

    /**
     * @param hostname       The URL of the Postgrest server
     *                       Example: postgrest.example.com
     * @param endpoint       The endpoint of the Postgrest server
     *                       Example: /rest/v1
     * @param anonymousToken The anonymous token of the Postgrest server
     */
    public PostgrestClient(@NotNull String hostname, String endpoint, @NotNull String anonymousToken) {
        this.hostname = hostname;
        this.endpoint = endpoint;
        this.anonymousToken = anonymousToken;

        this.httpClient = HttpClient.newBuilder().build();
    }

    public JsonElement sendQuery(@NotNull PostgrestQuery query) {
        HttpRequest.Builder requestBuilder;

        //Create the initial request uri
        try {
            requestBuilder = HttpRequest.newBuilder().uri(query.getUrlBuilder().build().toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Set the request method and body (if applicable)
        switch (query.getHttpMethod()) {
            case GET -> requestBuilder.GET();
            case POST -> requestBuilder.POST(HttpRequest.BodyPublishers.ofString(query.getBody().toString()));
            case PUT -> requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(query.getBody().toString()));
            case PATCH ->
                    requestBuilder.method("PATCH", HttpRequest.BodyPublishers.ofString(query.getBody().toString()));
            case DELETE -> requestBuilder.DELETE();
        }

        //Set the request headers (copy from PostgrestQuery)
        for (Map.Entry<String, List<String>> header : query.getHeaders().entrySet()) {
            for (String value : header.getValue()) {
                requestBuilder.header(header.getKey(), value);
            }
        }
        requestBuilder.header("apikey", anonymousToken);
        requestBuilder.header("Authorization", "Bearer " + anonymousToken);


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
}
