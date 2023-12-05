package org.deustomed.httputils;

import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UrlBuilder {

    private UrlScheme scheme;
    private String hostname;
    private int port;
    private String path;
    private Map<String, List<String>> queryParameters = new LinkedHashMap<>(); // Preserve insertion order

    public UrlBuilder() {
        this(UrlScheme.HTTP, "localhost");
    }

    public UrlBuilder(@NotNull UrlScheme scheme, @NotNull String hostname) {
        this.setScheme(scheme);
        this.setHostname(hostname);
        this.setPort(scheme.getDefaultPort());
    }

    public UrlBuilder(@NotNull UrlScheme scheme, @NotNull String hostname, int port) {
        this.setScheme(scheme);
        this.setHostname(hostname);
        this.setPort(port);
    }

    public UrlBuilder(@NotNull UrlBuilder urlBuilder) {
        this.scheme = urlBuilder.scheme;
        this.hostname = urlBuilder.hostname;
        this.port = urlBuilder.port;
        this.path = urlBuilder.path;
        this.queryParameters = urlBuilder.queryParameters;
    }

    public UrlBuilder setScheme(@NotNull UrlScheme scheme) {
        this.scheme = scheme;
        this.setPort(scheme.getDefaultPort());
        return this;
    }

    public UrlScheme getScheme() {
        return scheme;
    }

    public UrlBuilder setHostname(@NotNull String hostname) {
        if (hostname.isEmpty()) throw new IllegalArgumentException("Cannot query blank hostname");
        this.hostname = hostname;
        return this;
    }

    public String getHostname() {
        return hostname;
    }

    public UrlBuilder setPort(int port) {
        if (port < 0 || port > 65535) throw new IllegalArgumentException("Port must be between 0 and 65535");

        this.port = port;
        return this;
    }

    public int getPort() {
        return port;
    }

    public UrlBuilder setPath(@NotNull String path) {
        if (path.isEmpty()) throw new IllegalArgumentException("Cannot query blank path");

        this.path = path;
        return this;
    }

    public String getPath() {
        return path;
    }

    /**
     * @return The query parameters as a map of keys to lists of values
     */
    public Map<String, List<String>> getQueryParameters() {
        return queryParameters;
    }

    /**
     * @param key
     * @return The first value of the query parameter, or null if it doesn't exist
     */
    public String getQueryParameter(String key) {
        if (queryParameters.containsKey(key)) {
            return queryParameters.get(key).get(0);
        }
        return null;
    }

    /**
     * @param key
     * @return All the values of the query parameter, or null if it doesn't exist
     */
    public List<String> getQueryParameterList(String key) {
        if (queryParameters.containsKey(key)) {
            return queryParameters.get(key);
        }
        return null;
    }

    /**
     * Sets the query parameter to the given value, overwriting any existing values
     *
     * @param key
     * @param value
     * @return The UrlBuilder instance
     */
    public UrlBuilder setQueryParameter(@NotNull String key, @NotNull String value) {
        if (key.isEmpty()) throw new IllegalArgumentException("Cannot set blank key in query parameter");
        if (value.isEmpty()) throw new IllegalArgumentException("Cannot set blank value in query parameter");

        queryParameters.put(key, new ArrayList<>(List.of(value)));
        return this;
    }

    /**
     * Adds the query parameter to the given value, appending to any existing values
     *
     * @param key
     * @param value
     * @return The UrlBuilder instance
     */
    public UrlBuilder addQueryParameter(@NotNull String key, @NotNull String value) {
        if (key.isEmpty()) throw new IllegalArgumentException("Cannot add blank key to query parameters");
        if (value.isEmpty()) throw new IllegalArgumentException("Cannot add blank value to query parameters");

        queryParameters.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        return this;
    }

    /**
     * @param key
     * @return True if the query parameter exists, false otherwise
     */
    public boolean hasQueryParameter(String key) {
        return queryParameters.containsKey(key);
    }

    private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return The pathname of the URL, including the path and query parameters
     */
    public String getPathname() {
        StringBuilder stringBuilder = new StringBuilder();

        if (path != null) {
            stringBuilder.append(path);
        }

        if (!queryParameters.isEmpty()) {
            stringBuilder.append("?");
            String queryString =
                    queryParameters.entrySet().stream()
                            .flatMap(entry -> entry.getValue().stream().map(value -> entry.getKey() + "=" + value))
                            .collect(Collectors.joining("&"));
            stringBuilder.append(queryString);
        }
        return stringBuilder.toString();
    }

    /**
     * @return The URL
     */
    public URL build() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(scheme.toString());
        stringBuilder.append("://");
        stringBuilder.append(hostname);
        if (port != scheme.getDefaultPort()) {
            stringBuilder.append(":");
            stringBuilder.append(port);
        }

        stringBuilder.append(this.getPathname());

        try {
            return new URL(stringBuilder.toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return The URL as a string
     */
    public String toString() {
        return build().toString();
    }

}
