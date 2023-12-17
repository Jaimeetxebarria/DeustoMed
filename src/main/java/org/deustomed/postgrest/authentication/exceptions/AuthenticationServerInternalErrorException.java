package org.deustomed.postgrest.authentication.exceptions;

import java.io.Serial;

public class AuthenticationServerInternalErrorException extends PostgrestAuthenticationException {
    @Serial
    private static final long serialVersionUID = 1L;

    public AuthenticationServerInternalErrorException() {
        super();
    }

    public AuthenticationServerInternalErrorException(String message) {
        super(message);
    }

    public AuthenticationServerInternalErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationServerInternalErrorException(Throwable cause) {
        super(cause);
    }
}
