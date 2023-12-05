package org.deustomed.postgrest;

import com.google.gson.JsonElement;
import org.deustomed.httputils.HttpMethod;
import org.deustomed.httputils.UrlBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostgrestQuery {
    private HttpMethod httpMethod;
    private UrlBuilder urlBuilder;
    private Map<String, List<String>> headers;
    private JsonElement body;

    public PostgrestQuery() {
        httpMethod = HttpMethod.GET;
        urlBuilder = new UrlBuilder();
        headers = new HashMap<>();
        body = null;
    }

    public PostgrestQuery(HttpMethod httpMethod, UrlBuilder urlBuilder, Map<String, List<String>> headers,
                          JsonElement body) {
        this.httpMethod = httpMethod;
        this.urlBuilder = urlBuilder;
        this.headers = headers;
        this.body = body;
    }


    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(@NotNull HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public UrlBuilder getUrlBuilder() {
        return urlBuilder;
    }

    public void setUrlBuilder(@NotNull UrlBuilder urlBuilder) {
        this.urlBuilder = urlBuilder;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public List<String> getHeader(@NotNull String key) {
        if (key.isEmpty()) throw new IllegalArgumentException("Cannot get header with blank key");
        return headers.get(key);
    }

    public void setHeaders(@NotNull Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public void addHeader(@NotNull String key, @NotNull String value) {
        if (key.isEmpty()) throw new IllegalArgumentException("Cannot add header with blank key");

        this.headers.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    public JsonElement getBody() {
        return body;
    }

    public void setBody(@NotNull JsonElement body) {
        this.body = body;
    }
}
