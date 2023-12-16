package org.deustomed.authentication;

import org.deustomed.postgrest.PostgrestQuery;
import org.deustomed.postgrest.authentication.PostgrestAuthenticationService;
import org.deustomed.postgrest.authentication.exceptions.PostgrestAuthenticationException;

public class SuperuserAuthenticationService implements PostgrestAuthenticationService {
    private final String anonymousToken;
    private final String superuserToken;

    public SuperuserAuthenticationService(String anonymousToken, String superuserToken) {
        this.anonymousToken = anonymousToken;
        this.superuserToken = superuserToken;
    }

    @Override
    public void addAuthenticationHeaders(PostgrestQuery postgrestQuery) throws PostgrestAuthenticationException {
        postgrestQuery.addHeader("apikey", anonymousToken);
        postgrestQuery.addHeader("cookie", "accessToken=" + superuserToken);
    }
}
