package org.deustomed;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private String hostname;
    private String endpoint;
    private String anonymousToken;

    public ConfigLoader() {
        try (InputStream input = new FileInputStream("src/main/java/org/deustomed/DB.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            hostname = prop.getProperty("hostname");
            endpoint = prop.getProperty("endpoint");
            anonymousToken = prop.getProperty("anonymous_token");
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
}
