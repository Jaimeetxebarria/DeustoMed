package org.deustomed.authentication;

import org.deustomed.postgrest.PostgrestQuery;
import org.deustomed.postgrest.authentication.PostgrestAuthenticationService;
import org.deustomed.postgrest.authentication.exceptions.PostgrestAuthenticationException;

public class AnonymousAuthenticationService implements PostgrestAuthenticationService {
    private final String anonymousToken;

    public AnonymousAuthenticationService(String anonymousToken) {
        this.anonymousToken = anonymousToken;
    }

    @Override
    public void addAuthenticationHeaders(PostgrestQuery postgrestQuery) throws PostgrestAuthenticationException {
        postgrestQuery.addHeader("apikey", anonymousToken);
    }
}
