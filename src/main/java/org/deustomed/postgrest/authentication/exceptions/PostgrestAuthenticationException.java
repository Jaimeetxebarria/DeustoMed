package org.deustomed.postgrest.authentication.exceptions;

import java.io.Serial;

public class PostgrestAuthenticationException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public PostgrestAuthenticationException() {
        super();
    }

    public PostgrestAuthenticationException(String message) {
        super(message);
    }

    public PostgrestAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostgrestAuthenticationException(Throwable cause) {
        super(cause);
    }
}
