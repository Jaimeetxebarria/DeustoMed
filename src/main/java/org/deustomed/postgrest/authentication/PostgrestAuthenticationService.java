package org.deustomed.postgrest.authentication;

import org.deustomed.postgrest.PostgrestQuery;
import org.deustomed.postgrest.authentication.exceptions.PostgrestAuthenticationException;

public interface PostgrestAuthenticationService {
    void addAuthenticationHeaders(PostgrestQuery requestBuilder) throws PostgrestAuthenticationException;
}
