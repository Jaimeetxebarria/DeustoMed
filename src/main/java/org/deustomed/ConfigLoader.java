package org.deustomed;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private String hostname;
    private String endpoint;
    private String anonymousToken;
    private String authServerBaseUrl;

    public ConfigLoader() {
        try (InputStream input = new FileInputStream("src/main/java/org/deustomed/config.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            hostname = prop.getProperty("hostname");
            endpoint = prop.getProperty("endpoint");
            anonymousToken = prop.getProperty("anonymous_token");
            authServerBaseUrl = prop.getProperty("auth_server_base_url");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getHostname() {
        return hostname;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getAnonymousToken() {
        return anonymousToken;
    }

    public String getAuthServerBaseUrl() {
        return authServerBaseUrl;
    }
}
