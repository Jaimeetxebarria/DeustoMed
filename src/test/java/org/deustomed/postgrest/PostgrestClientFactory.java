package org.deustomed.postgrest;

import org.deustomed.authentication.AnonymousAuthenticationService;
import org.deustomed.authentication.SuperuserAuthenticationService;
import org.deustomed.authentication.UserAuthenticationService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.MissingResourceException;
import java.util.Properties;

/**
 * Factory class for creating PostgrestClient instances.
 * Strictly for testing purposes.
 */
public class PostgrestClientFactory {
    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(new FileInputStream("src/test/java/org/deustomed/postgrest/dbconfig.properties"));
        } catch (IOException e) {
            System.err.println("Error loading dbconfig.properties. Please make sure it exists and is readable.");
            System.exit(1);
        }
    }

    public static String getProperty(String key) {
        if (!properties.containsKey(key)) {
            throw new MissingResourceException("Property '" + key + "' not found", "PostgrestClientFactory", key);
        }
        return properties.getProperty(key);
    }

    public static PostgrestClient createAnonymousClient() {
        return new PostgrestClient(getProperty("hostname"), getProperty("endpoint"), new AnonymousAuthenticationService(getProperty(
                "anonymousToken")));
    }

    public static PostgrestClient createSuperuserClient() {
        return new PostgrestClient(getProperty("hostname"), getProperty("endpoint"),
                new SuperuserAuthenticationService(getProperty("anonymousToken"), getProperty("superuserToken")));
    }

    public static PostgrestClient createAuthenticatedClient(UserAuthenticationService userAuthenticationService) {
        return new PostgrestClient(getProperty("hostname"), getProperty("endpoint"), userAuthenticationService);
    }
}
