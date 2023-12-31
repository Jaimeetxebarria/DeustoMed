package org.deustomed.postgrest.authentication.exceptions;

import java.io.Serial;

public class AuthenticationServerUnavailableException extends PostgrestAuthenticationException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AuthenticationServerUnavailableException() {
        super("Authentication server unavailable");
    }

    public AuthenticationServerUnavailableException(String message) {
        super(message);
    }

    public AuthenticationServerUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationServerUnavailableException(Throwable cause) {
        super(cause);
    }
}
